package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.mapper.UserMapper;
import com.wdc.model.po.UserBean;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ServiceImpl<UserMapper, UserBean> {

}
