package com.wdc.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wdc.common.BaseResponse;
import com.wdc.common.ErrorCode;
import com.wdc.common.ResultUtils;
import com.wdc.exception.BusinessException;
import com.wdc.model.DTO.UserLoginRequest;
import com.wdc.model.DTO.UserRegisterDTO;
import com.wdc.model.po.UserBean;
import com.wdc.service.UserService;
import com.wdc.util.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.wdc.common.ErrorCode.*;

@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "*")
public class SUserController {

    @Resource
    private  UserService userService;

    @RequestMapping("/getAdminByName")
    public RestResponse getAdminByNameAndPassword(String username, String password) {
        if (username ==null || password == null) {
            return RestResponse.fail("用户名或者密码未输入");
        } else {
            UserBean userBean = new UserBean();

            userBean.setUname(username);
            userBean.setUpassword(password);
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uname",username);
            queryWrapper.eq("upassword",password);
            UserBean userBean1 = userService.getOne(queryWrapper);
            if (userBean1 == null) {
                return RestResponse.fail("用户名或者密码不正确");
            }
            return RestResponse.ok(userBean1);
        }

    }
    @RequestMapping("/getAdminById")
    public RestResponse getAdminById() {
        UserBean adminBean = userService.getById(1);
        return RestResponse.ok(adminBean);
    }


    /**
     * 用户注册
     * @param userRegisterRequest
     * @param request
     * @return
     */
    @RequestMapping("/register")
    public RestResponse<UserBean> userRegister(@RequestBody UserRegisterDTO userRegisterRequest , HttpServletRequest request){
        String userAccount = userRegisterRequest.getUloginname();
        String userPassword = userRegisterRequest.getUpassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword) ||StrUtil.isBlank(checkPassword) ) {
            throw new BusinessException(NULL_ERROR);
        }
        UserBean user = userService.userRegister(userRegisterRequest,request);
        return RestResponse.ok(user);
    }


    @PostMapping("/login")
    public BaseResponse<UserBean> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return ResultUtils.error(PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUloginname();
        String userPassword = userLoginRequest.getUpassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        UserBean user = userService.userLogin(userLoginRequest,request);
        if (user == null){
            return ResultUtils.error(NOT_LOGIN,"登录失败");
        }
        return ResultUtils.success(user);
    }




}



