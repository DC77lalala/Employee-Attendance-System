package com.wdc.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wdc.common.BaseResponse;
import com.wdc.common.ErrorCode;
import com.wdc.common.ResultUtils;
import com.wdc.exception.BusinessException;
import com.wdc.model.dao.EmploymentRequestDTO;
import com.wdc.model.dao.PostSignInRequestDTO;
import com.wdc.model.dao.UserRegisterDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.service.IEmployService;
import com.wdc.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.wdc.common.ErrorCode.NULL_ERROR;

@RestController
@RequestMapping("/employ")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployController {

    @Resource
    private IEmployService employService;


    /**
     * 增加员工
     */
    @RequestMapping("/register")
    public RestResponse<EmploymentBean> userRegister(@RequestBody UserRegisterDTO userRegisterRequest , HttpServletRequest request){
        String userAccount = userRegisterRequest.getUloginname();
        String userPassword = userRegisterRequest.getUpassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword) ||StrUtil.isBlank(checkPassword) ) {
            throw new BusinessException(NULL_ERROR);
        }
        EmploymentBean user = employService.userRegister(userRegisterRequest,request);
        return RestResponse.ok(user);
    }


    /**
     * 员工签到打卡
     */
    @RequestMapping(value = "/sign/in", method = RequestMethod.POST)
    public RestResponse<SignIn> postSignIn(@RequestBody PostSignInRequestDTO postSignInRequestDTO) {
        return employService.postSignIn(postSignInRequestDTO);
    }

    /**
     * 修改员工
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public RestResponse<EmploymentBean> update(@RequestBody EmploymentRequestDTO employmentRequestDTO ,Long employId) {
        EmploymentBean employment =  employService.update(employmentRequestDTO,employId);
        return RestResponse.ok(employment);

    }

    /**
     * 删除员工
     */
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public RestResponse<EmploymentBean> del(Long employId) {
        return employService.del(employId);
    }


    /**
     * 查询员工
     */
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public RestResponse<EmploymentBean> sel(Long employId) {
        return employService.del(employId);
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public BaseResponse<Page<EmploymentBean>> listTeamsByPage(EmploymentRequestDTO employmentRequestDTO) {
        if (employmentRequestDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        EmploymentBean team = new EmploymentBean();
        BeanUtils.copyProperties(employmentRequestDTO, team);
        Page<EmploymentBean> page = new Page<>(employmentRequestDTO.getPageNum(), employmentRequestDTO.getPageSize());
        QueryWrapper<EmploymentBean> queryWrapper = new QueryWrapper<>(team);
        Page<EmploymentBean> resultPage = employService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }




}
