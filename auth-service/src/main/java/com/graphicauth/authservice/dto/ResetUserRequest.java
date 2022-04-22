package com.graphicauth.authservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResetUserRequest {
    @NotBlank
    private String userName;

    @NotBlank()
    private String passPoints;

    @NotNull()
    private Long numberOfPassPoints;

    private Long tolerance;

    private Long canvasX;

    private Long canvasY;

    private Long imageRef;

    @NotBlank
    private String verifyToken;
}
