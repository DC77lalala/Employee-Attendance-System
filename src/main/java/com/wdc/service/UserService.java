package com.wdc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.common.ErrorCode;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.UserMapper;
import com.wdc.model.DTO.UserLoginRequestDTO;
import com.wdc.model.DTO.UserRegisterDTO;
import com.wdc.model.po.UserBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wdc.contant.UserConstant.USER_LOGIN_STATE;


@Service
public class UserService extends ServiceImpl<UserMapper, UserBean> {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "wdc";

    public UserBean userRegister(UserRegisterDTO userRegisterRequest, HttpServletRequest request) {

        //1.判断用户是否存在
        QueryWrapper<UserBean> queryWrapper = new QueryWrapper<>();
        String userAccount = userRegisterRequest.getUloginname();
        String userPassword = userRegisterRequest.getUpassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return null;
        }
        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        queryWrapper.eq("uloginname",userAccount);
        queryWrapper.eq("upassword",userPassword);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3. 插入数据
        UserBean user = new UserBean();
        user.setUloginname(userAccount);
        user.setUpassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return null;
        }
        return user;

    }

    public UserBean userLogin(UserLoginRequestDTO userLoginRequestDTO, HttpServletRequest request) {
        String userAccount = userLoginRequestDTO.getUloginname();
        String userPassword = userLoginRequestDTO.getUpassword();
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
        QueryWrapper<UserBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uloginname",userAccount);
        queryWrapper.eq("upassword",encryptPassword);
        UserBean user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return null;
        }
        //用户脱敏
        UserBean safetyUser = getSafetyUser(user);
        //记录用户的登陆的状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;

    }


    public UserBean getSafetyUser(UserBean originUser) {
        if (originUser == null){
            return null;
        }

        UserBean safetyUser = new UserBean();
        safetyUser.setUname(originUser.getUname());
        safetyUser.setUtype(originUser.getUtype());
        safetyUser.setUloginname(originUser.getUloginname());

        return safetyUser;
    }
}
