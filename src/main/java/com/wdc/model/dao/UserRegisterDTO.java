package com.wdc.model.dao;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String uloginname;
    private String upassword;
    private String CheckPassword;
}
