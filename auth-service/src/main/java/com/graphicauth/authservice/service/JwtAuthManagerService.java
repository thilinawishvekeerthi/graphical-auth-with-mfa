package com.graphicauth.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.graphicauth.authservice.security.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtAuthManagerService implements IJwtAuthManagerService{
    private final JwtConfiguration jwtConfiguration;

    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }

    @Override
    public DecodedJWT getClaimsFromJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    @Override
    public boolean validateToken(String token) throws JWTVerificationException{
        Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }
}
