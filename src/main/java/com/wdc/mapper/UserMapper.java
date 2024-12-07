package com.wdc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wdc.model.po.UserBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserBean> {

    UserBean selectById(Integer id);

}
