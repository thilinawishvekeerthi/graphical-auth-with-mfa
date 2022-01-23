package com.graphicauth.authservice.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphicauth.authservice.service.IJwtAuthManagerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CustomAuthPerRequestFilter extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfiguration;
    private final IJwtAuthManagerService jwtAuthManagerService;

    public CustomAuthPerRequestFilter(JwtConfiguration jwtConfiguration, IJwtAuthManagerService jwtAuthManagerService) {
        this.jwtConfiguration = jwtConfiguration;
        this.jwtAuthManagerService = jwtAuthManagerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/auth/sign-up")){
            filterChain.doFilter(request,response);
        }else{
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String tokenPreFix = jwtConfiguration.getPrefix()+" ";
            if(authorizationHeader == null || !authorizationHeader.startsWith(tokenPreFix)) {
                filterChain.doFilter(request, response);  		// If not valid, go to the next filter.
                return;
            }
            if(authorizationHeader != null && authorizationHeader.startsWith(tokenPreFix)){
                String token = authorizationHeader.substring(tokenPreFix.length());
                try {
                    if(this.jwtAuthManagerService.validateToken(token)){
                        DecodedJWT decodedJWT = this.jwtAuthManagerService.getClaimsFromJWT(token);
                        String user = decodedJWT.getSubject();
                        String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        Arrays.stream(roles).forEach(role->authorities.add(new SimpleGrantedAuthority(role)));
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request,response);
                    }
                }
                catch (TokenExpiredException tokenExpiredException){
                    Map<String, String> error = new HashMap<>();
                    error.put("Error_Code", "455");
                    error.put("Error_Message", tokenExpiredException.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
                catch (Exception exception){
                        Map<String, String> error = new HashMap<>();
                        error.put("Error_Message", exception.getMessage());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        new ObjectMapper().writeValue(response.getOutputStream(),error);
                }

            }
        }


    }
}
