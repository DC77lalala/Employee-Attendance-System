package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdc.model.dao.EmploymentRequestDTO;
import com.wdc.model.dao.PostSignInRequestDTO;
import com.wdc.model.dao.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.util.RestResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * employ 的接口
 */
public interface IEmployService extends IService<EmploymentBean> {
    EmploymentBean userRegister(UserRegisterDTO userRegisterRequest, HttpServletRequest request);

    /**
     * 员工签到
     * @param postSignInRequestDTO
     * @return
     */
    RestResponse<SignIn> postSignIn(PostSignInRequestDTO postSignInRequestDTO);

    EmploymentBean update(EmploymentRequestDTO employmentRequestDTO,Long employId);

    RestResponse<EmploymentBean> del(Long employId);
}
