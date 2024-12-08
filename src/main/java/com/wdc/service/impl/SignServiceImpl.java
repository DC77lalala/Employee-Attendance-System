package com.wdc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.mapper.SignMapper;
import com.wdc.model.po.SignIn;
import com.wdc.service.ISignService;
import com.wdc.util.RestResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, SignIn> implements ISignService {

    @Resource
    private SignMapper signMapper;

    @Override
    public RestResponse<SignIn> saveSignDate(SignIn signIn) {
           signMapper.saveSignDate(signIn);
        return RestResponse.ok();
    }
}
