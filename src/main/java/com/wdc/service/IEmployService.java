package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdc.model.DTO.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.UserBean;

import javax.servlet.http.HttpServletRequest;

/**
 * employ 的接口
 */
public interface IEmployService extends IService<EmploymentBean> {
    EmploymentBean userRegister(UserRegisterDTO userRegisterRequest, HttpServletRequest request);
}
