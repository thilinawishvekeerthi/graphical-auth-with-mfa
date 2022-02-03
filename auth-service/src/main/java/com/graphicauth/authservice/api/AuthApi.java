package com.graphicauth.authservice.api;

import com.graphicauth.authservice.dto.SignUpRequest;
import com.graphicauth.authservice.dto.SignUpResponse;
import com.graphicauth.authservice.dto.TotpVerifyRequest;
import com.graphicauth.authservice.service.ITotpAuthService;
import com.graphicauth.authservice.service.IUserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthApi {
    private final IUserService userService;
    private final ITotpAuthService totpAuthService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUpUser(@Valid @RequestBody SignUpRequest request) throws QrGenerationException, SQLException {
        URI location = URI.create( ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/sign-up").toUriString());
        return ResponseEntity.created(location).body(userService.signUpUser(request));
    }
    @PostMapping("/totp/verify")
    public ResponseEntity<Boolean> verifyTotp(@Valid @RequestBody TotpVerifyRequest request) {
        return ResponseEntity.ok().body(totpAuthService.verifyCodeByUser(request.getTotp(), request.getUserName()));
    }

}
