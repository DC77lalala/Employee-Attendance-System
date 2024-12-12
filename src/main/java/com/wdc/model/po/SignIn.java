package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sign_in")
public class SignIn {

    @TableId(value = "id" ,type = IdType.AUTO)
    private Long id;

    /**
     * 员工编号
     */
    @TableField(value = "employ_idcard")
    private String employIdcard;

    /**
     * 打卡人
     */
    @TableField(value = "employ_name")
    private String employName;

    /**
     * 签到地点
     */
    @TableField(value = "sign_address")
    private String signAddress;


    /**
     * 签到日期（精确到日）
     */
    @TableField(value = "sign_in_date")
    private Date signInDate;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;


}
