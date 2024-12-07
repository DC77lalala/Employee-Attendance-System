package com.wdc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSignInEntity {

    /**
     * 业务编码
     */
    private String buNo;
    /**
     * 员工编号
     */
    private String employId;
    /**
     * 打卡人
     */
    private String employName;
    /**
     * 打卡次数
     */
    private Integer signCount;
    /**
     * 签到地点
     */
    private String signAddress;
    /**
     * 签到日期（精确到日）
     */
    private Date signInDate;



}
