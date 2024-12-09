package com.wdc.model.DTO;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String uloginname;
    private String upassword;
    private String checkPassword;
}
