package com.wdc.controller;

import com.wdc.common.BaseResponse;
import com.wdc.common.ErrorCode;
import com.wdc.common.ResultUtils;
import com.wdc.exception.BusinessException;
import com.wdc.model.DTO.LeavePersonReqDTO;
import com.wdc.model.po.EmploymentBean;
import com.wdc.model.po.Leave;
import com.wdc.service.IEmployService;
import com.wdc.service.ILeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
@Slf4j
public class LeaveController {

    @Resource
    private ILeaveService leaveService;

    @Resource
    private IEmployService employService;
    /**
     * 查询所有请假记录
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResponse<List<Leave>> applyLeave() {
        List<Leave> list = leaveService.list();
        return ResultUtils.success(list);

    }

    /**
     * 查询自己的请假记录
     *
     * @return
     */
    @RequestMapping(value = "/listPerson", method = RequestMethod.POST)
    public BaseResponse<List<Leave>> leaveByPerson(@RequestBody LeavePersonReqDTO leaveReq, HttpServletRequest request) {
        EmploymentBean loginUser = employService.getLoginUser(request);
        log.info("loginUser: {} , leaveReq: {}", loginUser, leaveReq);
        if (!Objects.equals(loginUser.getIdcard(), leaveReq.getIdcard())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只能查询自己的请假记录");
        }

        List<Leave> list = leaveService.leaveByPerson(leaveReq);
        return ResultUtils.success(list);

    }


    /**
     * update
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResponse<Leave> updateState(@RequestBody Leave leaveReq, HttpServletRequest request) {
        //获取当前登录对象
        EmploymentBean loginUser = employService.getLoginUser(request);
        Integer isAdmin = loginUser.getIsAdmin();
        if (isAdmin != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "没有权限");
        }
         return ResultUtils.success(leaveService.updateState(leaveReq));


    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<Leave> addLeave(@RequestBody Leave leaveReq, HttpServletRequest request) {


        // 根据请求创建新的 Leave 对象
        Leave leave = new Leave();
        leave.setLeavePerson(leaveReq.getLeavePerson());
        leave.setLeaveType(leaveReq.getLeaveType());
        leave.setStartTime(leaveReq.getStartTime());
        leave.setEndTime(leaveReq.getEndTime());
        leave.setReason(leaveReq.getReason());
        leave.setStatus(leaveReq.getStatus());
        leave.setIdcard(leaveReq.getIdcard());

        // 使用 MyBatis-Plus 插入数据

        int insertCount = leaveService.insertLeave(leave);  // 插入数据库

        if (insertCount > 0) {
            return ResultUtils.success(leave);  // 返回成功
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "新增请假记录失败");
        }
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse<String> deleteLeave(@RequestBody Leave leaveReq, HttpServletRequest request) {
        // 获取当前登录对象
        EmploymentBean loginUser = employService.getLoginUser(request);
        Integer isAdmin = loginUser.getIsAdmin();

        // 判断是否是管理员
        if (isAdmin != 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "没有权限");
        }



        Integer deleteCount =  leaveService.deleteLeave(leaveReq);



        if (deleteCount > 0) {
            return ResultUtils.success("删除成功");  // 返回删除成功消息
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
    }



}
