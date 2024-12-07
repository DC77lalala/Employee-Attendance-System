package com.wdc.model.dao;

import lombok.Data;

@Data
public class PostSignInRequestDTO {
    /**
     * 员工编号
     */
    private String employId;
    /**
     * 打卡人
     */
    private String employName;
    /**
     * 签到地点
     */
    private String signAddress;


}
