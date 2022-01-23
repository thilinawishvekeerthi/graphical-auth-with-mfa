package com.graphicauth.authservice.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;

public interface IJwtAuthManagerService {
    String generateToken(Authentication authentication);
    DecodedJWT getClaimsFromJWT(String token);
    boolean validateToken(String token) throws Exception;
}
