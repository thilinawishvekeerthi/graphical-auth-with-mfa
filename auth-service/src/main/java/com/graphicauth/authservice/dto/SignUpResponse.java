package com.graphicauth.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {
    private boolean mfa;
    private String secretImageUri;
}
