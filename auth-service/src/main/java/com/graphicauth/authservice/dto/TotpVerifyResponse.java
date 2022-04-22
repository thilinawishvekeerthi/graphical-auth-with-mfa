package com.graphicauth.authservice.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotpVerifyResponse {
    private boolean isVerified;
    private String veryToken;
}
