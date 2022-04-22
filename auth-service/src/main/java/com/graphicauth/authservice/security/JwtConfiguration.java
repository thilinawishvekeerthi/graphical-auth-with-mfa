package com.graphicauth.authservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class JwtConfiguration {

    @Value("${app.security.jwt.uri}")
    private String uri;

    @Value("${app.security.jwt.header}")
    private String header;

    @Value("${app.security.jwt.prefix}")
    private String prefix;

    @Value("${app.security.jwt.expiration}")
    private int expiration;

    @Value("${app.security.jwt.secret}")
    private String secret;
}
