package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("employment")
public class EmploymentBean {
    @TableId(value = "eid" ,type = IdType.AUTO)
    private Integer eid;

    @TableField(value = "ename")
    private String ename;

    @TableField(value = "eage")
    private Integer eage;

    @TableField(value = "idcard")
    private String idcard;

    @TableField(value = "eaddr")
    private String eaddr;

    @TableField(value = "sex")
    private String sex;

    @TableField(value = "password")
    private String password;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "bio")
    private String bio;

    @TableField(value = "department")
    private String department;

    @TableField(value = "positions")
    private String positions;

    @TableField(value = "is_admin")
    private Integer isAdmin;

    @TableField(value = "create_time")
    private Date creteTime;

    @TableField(value = "update_time")
    private Date updateDate;


}
