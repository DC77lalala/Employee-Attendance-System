package com.wdc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wdc.model.po.SignIn;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignMapper extends BaseMapper<SignIn> {
    void saveSignDate(SignIn signIn);
}
