package com.graphicauth.authservice.service;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface ITotpAuthService {
    String getUriForImage(String secret) throws QrGenerationException;
    boolean verifyCode(String code, String secret);
    String generateSecret();
}
