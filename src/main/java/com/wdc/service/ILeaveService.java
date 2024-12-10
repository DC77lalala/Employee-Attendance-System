package com.wdc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdc.model.po.Leave;

public interface ILeaveService extends IService<Leave> {

    Leave updateState(Leave leaveReq);

    int insertLeave(Leave leave);

    Integer deleteLeave(Leave leaveReq);
}
