package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdc.model.po.SignIn;
import com.wdc.util.RestResponse;

public interface ISignService extends IService<SignIn> {

    RestResponse<SignIn> saveSignDate(SignIn signIn);
}
