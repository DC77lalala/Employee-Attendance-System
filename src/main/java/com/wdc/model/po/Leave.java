package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("leave_tb")
public class Leave {
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;


    @TableField(value = "idcard")
    private String idcard;

    @TableField(value = "leave_person")
    private String leavePerson;

    @TableField(value = "leave_type")
    private String leaveType;

    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "end_time")
    private Date endTime;

    @TableField(value = "reason")
    private String reason;

    @TableField(value = "status")
    private String status;


}
