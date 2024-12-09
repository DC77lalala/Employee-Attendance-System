package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdc.model.DTO.*;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.util.RestResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * employ 的接口
 */
public interface IEmployService extends IService<EmploymentBean> {
    EmploymentBean userRegister(EmploymentRegisterDTO userRegisterRequest, HttpServletRequest request);

    /**
     * 员工签到
     * @param postSignInRequestDTO
     * @return
     */
    RestResponse<SignIn> postSignIn(PostSignInRequestDTO postSignInRequestDTO);

    EmploymentBean updateBw(EmploymentRequestDTO employmentRequestDTO,Long employId);

    RestResponse<EmploymentBean> del(Long employId);

    EmploymentBean getLoginUser(HttpServletRequest request);

    int updateB(EmploymentBean employmentBean);

    EmploymentBean userLogin(EmployLoginRequestDTO employLoginRequestDTO, HttpServletRequest request);


//    int updateUser(EmploymentBean employmentBean, EmploymentBean loginUser);
}
