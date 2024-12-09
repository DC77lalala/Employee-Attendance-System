package com.wdc.model.DTO;

import lombok.Data;

@Data
public class PostSignInRequestDTO {
    /**
     * 员工编号
     */
    private Integer employId;
    /**
     * 打卡人
     */
    private String employName;
    /**
     * 签到地点
     */
    private String signAddress;
    /**
     * 员工编号
     */
    private String idcard;



}
