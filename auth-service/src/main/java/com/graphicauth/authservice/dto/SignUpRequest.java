package com.graphicauth.authservice.dto;

import com.graphicauth.authservice.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class SignUpRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String passWord;

    @NotBlank
    private String email;

    private boolean active;

    private boolean mfa;

    @NotBlank()
    private String passPoints;

    @NotNull()
    private Long numberOfPassPoints;

    @NotNull()
    private Long tolerance;

    @NotNull()
    private Long canvasX;

    @NotNull()
    private Long canvasY;

    @NotNull()
    private Long imageRef;
}
