package com.wdc.controller;


import cn.hutool.core.util.StrUtil;
import com.wdc.exception.BusinessException;
import com.wdc.model.DTO.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.model.po.UserBean;
import com.wdc.service.IEmployService;
import com.wdc.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.wdc.common.ErrorCode.NULL_ERROR;

@RestController
@RequestMapping("/employ")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployController {

    @Resource
    private IEmployService employService;


    /**
     * 增加员工
     */
    @RequestMapping("/register")
    public RestResponse<EmploymentBean> userRegister(@RequestBody UserRegisterDTO userRegisterRequest , HttpServletRequest request){
        String userAccount = userRegisterRequest.getUloginname();
        String userPassword = userRegisterRequest.getUpassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword) ||StrUtil.isBlank(checkPassword) ) {
            throw new BusinessException(NULL_ERROR);
        }
        EmploymentBean user = employService.userRegister(userRegisterRequest,request);
        return RestResponse.ok(user);
    }


    /**
     * 员工签到打卡
     */
    @RequestMapping(value = "/sign/in", method = RequestMethod.POST)
    public RestResponse<SignIn> postSignIn(@RequestBody PostSignInRequest postSignInRequest) {

    }




}
