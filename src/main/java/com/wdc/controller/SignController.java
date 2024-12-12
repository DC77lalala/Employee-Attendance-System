package com.wdc.controller;


import com.wdc.common.BaseResponse;
import com.wdc.common.ResultUtils;
import com.wdc.model.po.Leave;
import com.wdc.model.po.SignIn;
import com.wdc.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sign")
@CrossOrigin(origins = "*")
@Slf4j
public class SignController {

    @Resource
    private ISignService signService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResponse<List<SignIn>> applyLeave() {
        List<SignIn> list = signService.list();
        return ResultUtils.success(list);

    }


}
