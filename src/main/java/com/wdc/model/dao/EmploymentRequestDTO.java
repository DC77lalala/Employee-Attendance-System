package com.wdc.model.dao;

import com.wdc.common.PageRequest;
import lombok.Data;

@Data
public class EmploymentRequestDTO extends PageRequest {

    private String ename;
    private Integer eage;
    private String sex;
    private String eaddr;
    private String idcard;
    private String password;

}
