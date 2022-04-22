package com.graphicauth.authservice.service;

import com.graphicauth.authservice.dto.*;
import com.graphicauth.authservice.entity.Role;
import com.graphicauth.authservice.entity.User;
import dev.samstevens.totp.exceptions.QrGenerationException;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addUserRole(String userName, String roleName);
    User getUser(String userName);
    List<UserDto> getAllUsers();
    SignUpResponse signUpUser(SignUpRequest request) throws QrGenerationException, SQLException;
    ConfigDto getUserConfig(String userName);
    Boolean resetUser(ResetUserRequest resetUserRequest);
}
