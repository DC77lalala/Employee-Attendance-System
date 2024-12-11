package com.wdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdc.common.ErrorCode;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.EmploymentMapper;
import com.wdc.model.DTO.EmployLoginRequestDTO;
import com.wdc.model.DTO.EmploymentRegisterDTO;
import com.wdc.model.DTO.PostSignInRequestDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.redis.IRedisService;
import com.wdc.service.IEmployService;
import com.wdc.service.ISignService;
import com.wdc.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wdc.contant.UserConstant.USER_LOGIN_STATE;

/**
 * employ 的实现类
 */
@Service
@Slf4j
public class EmployServiceImpl extends ServiceImpl<EmploymentMapper, EmploymentBean> implements IEmployService {

    private static final String SALT = "wdc";
    @Resource
    private EmploymentMapper employmentMapper;

    @Resource
    private ISignService signService;
    @Resource
    private IRedisService redisService;


    @Value("${baidu.map.api.key}")
    private String apiKey;


    @Resource
    private   RestTemplate restTemplate;


    @Resource
    private ObjectMapper objectMapper;

    @Override
    public EmploymentBean userRegister(EmploymentRegisterDTO employmentRegisterDTO, HttpServletRequest request) {

        //1.判断用户是否存在
        String userAccount = employmentRegisterDTO.getEname();
        String userPassword = employmentRegisterDTO.getPassword();
        String checkPassword = employmentRegisterDTO.getCheckPassword();
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return null;
        }
        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ename", userAccount);
        queryWrapper.eq("password", encryptPassword);
        long count = employmentMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3. 插入数据
        EmploymentBean employmentBean = new EmploymentBean();
        employmentBean.setEname(userAccount);
        employmentBean.setPassword(encryptPassword);
        boolean saveResult = this.save(employmentBean);
        if (!saveResult) {
            return null;

        }
        EmploymentBean loginEmploy = employmentMapper.selectById(employmentBean.getEid());

        return getSafetyUser(loginEmploy);
    }

    @Override
    public RestResponse<SignIn> postSignIn(PostSignInRequestDTO postSignInRequestDTO) {
        //获取当前时间
//        String today = DateUtil.today();
//        Date dateTime = DateUtil.parse(today, "yyyy-MM-dd");
         //转换数据
        SignIn signIn = new SignIn();

        double latitude = postSignInRequestDTO.getLatitude();
        double longitude = postSignInRequestDTO.getLongitude();

        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey +
                "&output=json&coordtype=wgs84ll&location=" + latitude + "," + longitude;
        // 发送GET请求，获取API返回的地理信息

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String body = response.getBody();
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            JsonNode resultNode = jsonNode.path("result");
            String formattedAddress = resultNode.path("formatted_address").asText();

            signIn.setSignAddress(formattedAddress);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        signIn.setEmployIdcard(postSignInRequestDTO.getIdcard());
        signIn.setEmployName(postSignInRequestDTO.getEmployName());
        return signService.saveSignDate(signIn);

    }



    @Override
    public RestResponse<EmploymentBean> del(Long employId) {
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("eid", employId);
        EmploymentBean employmentBean  = employmentMapper.selectOne(queryWrapper);
        if (employmentBean  == null) return RestResponse.fail("员工不存在");
        int i = employmentMapper.deleteById(employId);
        if (i > 0) {
            return RestResponse.ok(employmentBean);
        } else {
            return RestResponse.fail("删除失败");
        }

    }

    @Override
    public EmploymentBean getLoginUser(HttpServletRequest request) {
        Object userObj = redisService.getValue(USER_LOGIN_STATE);

        return (EmploymentBean) userObj;
    }

    @Override
    public int updateB(EmploymentBean employmentBean) {
        int i = employmentMapper.updateById(employmentBean);
        return i;
    }

    @Override
    public EmploymentBean userLogin(EmployLoginRequestDTO employLoginRequestDTO, HttpServletRequest request) {
        String userAccount = employLoginRequestDTO.getIdcard();
        String userPassword = employLoginRequestDTO.getPassword();
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }
        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idcard",userAccount);
        queryWrapper.eq("password",encryptPassword);
        EmploymentBean user = employmentMapper.selectOne(queryWrapper);
        if (user == null){
            return null;
        }
        //用户脱敏
        EmploymentBean safetyUser = getSafetyUser(user);
        //记录用户的登陆的状态

        redisService.setValue(USER_LOGIN_STATE,safetyUser);

        log.info("存入 session 的用户: {} ",safetyUser);
        return safetyUser;

    }

    public EmploymentBean getSafetyUser(EmploymentBean originUser) {
        if (originUser == null){
            return null;
        }

        EmploymentBean safetyUser = new EmploymentBean();
        safetyUser.setIdcard(originUser.getIdcard());
        safetyUser.setEid(originUser.getEid());
        safetyUser.setBio(originUser.getBio());
        safetyUser.setDepartment(originUser.getDepartment());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setEname(originUser.getEname());
        safetyUser.setIsAdmin(originUser.getIsAdmin());
        safetyUser.setEaddr(originUser.getEaddr());
        safetyUser.setSex(originUser.getSex());
        safetyUser.setEage(originUser.getEage());
        safetyUser.setAvatar(originUser.getAvatar());

        return safetyUser;
    }

//    @Override
//    public int updateUser(EmploymentBean employmentBean, EmploymentBean loginUser) {
//        Long userId = Long.valueOf(loginUser.getEid());
//        if (userId == null){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//
//        //如果是管理员，允许更新任意用户
//        //如果不是
//        if ( !isAdmin(loginUser) && userId != loginUser.getEid() ){
//            throw new BusinessException(ErrorCode.NO_AUTH);
//
//        }
//        User oldUser = userMapper.selectById(userId);
//        if (oldUser == null){
//            throw new BusinessException(ErrorCode.NULL_ERROR);
//        }
//        return   userMapper.updateById(user);
//    }
}
