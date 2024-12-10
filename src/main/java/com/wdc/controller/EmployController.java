package com.wdc.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wdc.common.BaseResponse;
import com.wdc.common.ErrorCode;
import com.wdc.common.ResultUtils;
import com.wdc.exception.BusinessException;
import com.wdc.model.DTO.*;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.SignIn;
import com.wdc.service.IEmployService;
import com.wdc.service.ISignService;
import com.wdc.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.wdc.common.ErrorCode.*;

@RestController
@RequestMapping("/employ")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployController {

    @Resource
    private IEmployService employService;
    @Resource
    private ISignService signService;


    /**
     * 增加员工
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public RestResponse<EmploymentBean> userRegister(@RequestBody EmploymentRegisterDTO employmentRegisterDTO , HttpServletRequest request){
        String userAccount = employmentRegisterDTO.getEname();
        String userPassword = employmentRegisterDTO.getPassword();
        String checkPassword = employmentRegisterDTO.getCheckPassword();
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword) ||StrUtil.isBlank(checkPassword) ) {
            throw new BusinessException(NULL_ERROR);
        }
        EmploymentBean user = employService.userRegister(employmentRegisterDTO,request);
        return RestResponse.ok(user);
    }


    @PostMapping("/login")
    public RestResponse<EmploymentBean> userLogin(@RequestBody EmployLoginRequestDTO employLoginRequestDTO, HttpServletRequest request){
        if (employLoginRequestDTO == null){
            return RestResponse.fail("登录信息不明确");
        }
        String userAccount = employLoginRequestDTO.getIdcard();
        String userPassword = employLoginRequestDTO.getPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return RestResponse.fail("ErrorCode.PARAMS_ERROR");
        }

        EmploymentBean user = employService.userLogin(employLoginRequestDTO,request);
        if (user == null){
            return RestResponse.fail("NOT_LOGIN,登录失败");
        }
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
    public BaseResponse<Integer> updateUser(@RequestBody EmploymentBean employmentBean , HttpServletRequest request){
        //1.校验参数是否为空
        if (employmentBean == null){
            throw new RuntimeException("参数错误");
        }
        //2.校验用户权限
        //3.触发更新
//        EmploymentBean loginUser = employService.getLoginUser(request);

//        int result = employService.updateUser(employmentBean,loginUser);
        int result = employService.updateB(employmentBean);
        return ResultUtils.success(result);
    }

    /**
     * 删除员工
     */
    @RequestMapping(value = "/del",method = RequestMethod.GET)
    public RestResponse<EmploymentBean> del( Long employId) {
        return employService.del(employId);
    }



//    /**
//     * 查询员工
//     */
//    @RequestMapping(value = "/sel",method = RequestMethod.GET)
//    public RestResponse<EmploymentBean> sel(Long employId) {
//        return employService.del(employId);
//    }

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


    /**
     * 查询所有
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResponse<Integer> getAllUser() {

        List<EmploymentBean> list = employService.list();
        return ResultUtils.success(list.size());

    }


    @RequestMapping(value = "/applyLeave",method = RequestMethod.GET)
    public BaseResponse<Integer> applyLeave() {

        List<EmploymentBean> list = employService.list();
        return ResultUtils.success(list.size());

    }



}
