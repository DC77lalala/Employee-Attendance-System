package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("leave")
public class Leave {
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;

    @TableField(value = "leave_person")
    private String leavePerson;

    @TableField(value = "leave_type")
    private Integer leaveType;

    @TableField(value = "start_time")
    private String startTime;

    @TableField(value = "end_time")
    private String endTime;

    @TableField(value = "reason")
    private String reason;

    @TableField(value = "status")
    private String status;

}
