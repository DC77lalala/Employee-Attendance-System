package com.wdc.model.DTO;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String uloginname;
    private String upassword;
}
