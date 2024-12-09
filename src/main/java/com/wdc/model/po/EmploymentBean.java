package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("employment")
public class EmploymentBean {
    @TableId(value = "eid" ,type = IdType.AUTO)
    private Integer eid;
    private String ename;
    private Integer eage;
    private String idcard;
    private String eaddr;
    private String sex;
    private String password;
    private String email;
    private String phone;
    private String bio;
    private String department;
    private Date creteTime;
    private Date updateDate;

}
