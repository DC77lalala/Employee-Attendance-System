package com.wdc.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employment")
public class EmploymentBean {
    @TableId(value = "eid" ,type = IdType.AUTO)
    private Integer eid;
    private String ename;
    private Integer eage;
    private String sex;
    private String eaddr;
    private String idcard;
    private String password;

}
