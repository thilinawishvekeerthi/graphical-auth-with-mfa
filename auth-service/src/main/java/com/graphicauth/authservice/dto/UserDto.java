package com.graphicauth.authservice.dto;

import com.graphicauth.authservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String userName;

    private String email;

    private boolean active;

    private boolean mfa;

    private Collection<Role> roles = new ArrayList<>();
}
