package com.wdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdc.common.ErrorCode;
import com.wdc.common.ResultUtils;
import com.wdc.exception.BusinessException;
import com.wdc.mapper.LeaveMapper;
import com.wdc.model.DTO.LeavePersonReqDTO;
import com.wdc.model.po.Leave;
import com.wdc.service.ILeaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 请假的实现类
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {

    @Resource
    private LeaveMapper leaveMapper;

    @Override
    public Leave updateState(Leave leaveReq) {


        Leave leave = leaveMapper.selectOne(new QueryWrapper<Leave>().eq("id", leaveReq.getId()));

        if (leave == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "未找到对应的请假记录");
        }
        leave.setLeavePerson(leaveReq.getLeavePerson());
        leave.setLeaveType(leaveReq.getLeaveType());
        leave.setStartTime(leaveReq.getStartTime());
        leave.setEndTime(leaveReq.getEndTime());
        leave.setReason(leaveReq.getReason());
        leave.setStatus(leaveReq.getStatus());
        boolean updateSuccess = leaveMapper.updateById(leave) > 0;


        if (updateSuccess) {
            return leave;
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
    }

    @Override
    public int insertLeave(Leave leave) {
        return   leaveMapper.insert(leave);

    }

    @Override
    public Integer deleteLeave(Leave leaveReq) {
        String idcard = leaveReq.getIdcard();
        Leave leave = leaveMapper.selectOne(new QueryWrapper<Leave>().eq("idcard", idcard));

        if (leave == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到该身份证对应的请假记录");
        }

        return leaveMapper.delete(new QueryWrapper<Leave>().eq("idcard", idcard));
    }

    @Override
    public List<Leave> leaveByPerson(LeavePersonReqDTO leaveReq) {
        String idcard = leaveReq.getIdcard();
        if (idcard == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        List<Leave> list = leaveMapper.selectList(new QueryWrapper<Leave>().eq("idcard", idcard));
        return list;
    }
}
