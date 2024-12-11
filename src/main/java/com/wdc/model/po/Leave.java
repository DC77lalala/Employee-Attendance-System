package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField(value = "reason")
    private String reason;

    @TableField(value = "status")
    private String status;


}
