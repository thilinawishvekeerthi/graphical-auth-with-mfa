package com.graphicauth.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TotpVerifyRequest {
    private String totp;
    private String userName;
}
