package com.wdc.model.po;

import lombok.Data;

import java.util.Date;

@Data
public class SignIn {


    private Long id;
    /**
     * 业务编码
     */
    private String buNo;
    /**
     * 员工编号
     */
    private String employIdcard;

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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


}
