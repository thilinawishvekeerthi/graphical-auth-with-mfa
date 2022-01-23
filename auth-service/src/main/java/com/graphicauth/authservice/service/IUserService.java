package com.graphicauth.authservice.service;

import com.graphicauth.authservice.dto.SignUpRequest;
import com.graphicauth.authservice.dto.SignUpResponse;
import com.graphicauth.authservice.dto.UserDto;
import com.graphicauth.authservice.entity.Role;
import com.graphicauth.authservice.entity.User;
import dev.samstevens.totp.exceptions.QrGenerationException;

import java.util.List;

public interface IUserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addUserRole(String userName, String roleName);
    User getUser(String userName);
    List<UserDto> getAllUsers();
    SignUpResponse signUpUser(SignUpRequest request) throws QrGenerationException;
}
