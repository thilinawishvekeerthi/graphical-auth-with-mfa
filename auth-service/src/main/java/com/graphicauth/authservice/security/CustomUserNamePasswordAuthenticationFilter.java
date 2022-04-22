package com.graphicauth.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphicauth.authservice.entity.User;
import com.graphicauth.authservice.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class CustomUserNamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final JwtConfiguration jwtConfiguration;
    private final ITotpAuthService totpAuthService;
    private final IGraphicAuthService graphicAuthService;

    public CustomUserNamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtConfiguration jwtConfiguration, ITotpAuthService totpAuthService, IGraphicAuthService graphicAuthService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtConfiguration = jwtConfiguration;
        this.totpAuthService = totpAuthService;
        this.graphicAuthService = graphicAuthService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        String totp = request.getParameter("totp") == null ||  request.getParameter("totp").equals("null") ? null : request.getParameter("totp");
        User user = userService.getUser(userName);
        String decryptPasPoints = AESCipherService.decrypt(user.getPassPoints());

        if(Boolean.TRUE.equals(graphicAuthService.authenticate(decryptPasPoints,passWord,user.getNumberOfPassPoints(),10L))){
            passWord = decryptPasPoints;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,passWord);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if((authentication.isAuthenticated() && totp == null) || (authentication.isAuthenticated() && totp != null && !totpAuthService.verifyCode(totp, user.getTotpSecret()))) {
            authentication.setAuthenticated(false);
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String totp = request.getParameter("totp") == null ||  request.getParameter("totp").equals("null") ? null : request.getParameter("totp");
        if(authResult.isAuthenticated() && totp != null){
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(jwtConfiguration.getSecret().getBytes());
            String accessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration()))
                    .withIssuer(request.getRequestURI())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
            String refreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration()))
                    .withIssuer(request.getRequestURI())
                    .sign(algorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }else if(!authResult.isAuthenticated() && totp == null){

            Map<String, Boolean> tokens = new HashMap<>();
            tokens.put("two_factor_auth", true);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }else{
            Map<String, Boolean> authFail = new HashMap<>();
            authFail.put("auth-fail", true);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            new ObjectMapper().writeValue(response.getOutputStream(),authFail);
        }

    }
}
