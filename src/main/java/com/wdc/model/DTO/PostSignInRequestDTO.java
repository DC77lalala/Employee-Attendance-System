package com.wdc.model.DTO;

import lombok.Data;

@Data
public class PostSignInRequestDTO {

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


    /**
     * 维度
     */
    private double latitude;

    /**
     * 精度
     */
    private double longitude;



}
