package com.graphicauth.authservice.service;

import com.graphicauth.authservice.dto.TotpVerifyResponse;
import dev.samstevens.totp.exceptions.QrGenerationException;

public interface ITotpAuthService {
    String getUriForImage(String secret) throws QrGenerationException;
    boolean verifyCode(String code, String secret);
    boolean verifyCodeByUser(String code, String userName);
    TotpVerifyResponse verifyTotp(String code, String userName);
    String generateSecret();
}
