package com.wdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.common.ErrorCode;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.EmploymentMapper;
import com.wdc.model.dao.EmployLoginRequestDTO;
import com.wdc.model.dao.EmploymentRegisterDTO;
import com.wdc.model.dao.EmploymentRequestDTO;
import com.wdc.model.dao.PostSignInRequestDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.model.po.UserBean;
import com.wdc.service.IEmployService;
import com.wdc.service.ISignService;
import com.wdc.util.DateUtil;
import com.wdc.util.RestResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wdc.contant.UserConstant.USER_LOGIN_STATE;

/**
 * employ 的实现类
 */
@Service
public class EmployServiceImpl extends ServiceImpl<EmploymentMapper, EmploymentBean> implements IEmployService {

    private static final String SALT = "wdc";
    @Resource
    private EmploymentMapper employmentMapper;

    @Resource
    private ISignService signService;


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
        return employmentBean;
    }

    @Override
    public RestResponse<SignIn> postSignIn(PostSignInRequestDTO postSignInRequestDTO) {
        //获取当前时间
//        String today = DateUtil.today();
//        Date dateTime = DateUtil.parse(today, "yyyy-MM-dd");
         //转换数据
        SignIn signIn = new SignIn();
        signIn.setEmployIdcard(postSignInRequestDTO.getIdcard());
        signIn.setEmployName(postSignInRequestDTO.getEmployName());
        signIn.setSignAddress(postSignInRequestDTO.getSignAddress());
        signIn.setBuNo(RandomStringUtils.randomNumeric(11));
        return signService.saveSignDate(signIn);

    }

    @Override
    public EmploymentBean updateBw(EmploymentRequestDTO employmentRequestDTO, Long employId) {
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("eid", employId);
        EmploymentBean employmentBean  = employmentMapper.selectOne(queryWrapper);
        if (employmentBean  == null) return null;

        BeanUtils.copyProperties(employmentRequestDTO, employmentBean);

        int updateResult = employmentMapper.updateById(employmentBean);
        if (updateResult > 0) {
            return employmentBean;
        } else {
            return null;
        }


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
        if (request == null){
            return null;
        }

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
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
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;

    }

    public EmploymentBean getSafetyUser(EmploymentBean originUser) {
        if (originUser == null){
            return null;
        }

        EmploymentBean safetyUser = new EmploymentBean();
        safetyUser.setIdcard(originUser.getIdcard());
        safetyUser.setEid(originUser.getEid());
        safetyUser.setEname(originUser.getEname());
        safetyUser.setEaddr(originUser.getEaddr());
        safetyUser.setSex(originUser.getSex());
        safetyUser.setEage(originUser.getEage());

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
