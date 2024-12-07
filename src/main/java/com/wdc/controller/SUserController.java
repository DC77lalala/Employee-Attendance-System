package com.wdc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wdc.model.po.UserBean;
import com.wdc.service.UserService;
import com.wdc.util.RestResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}



