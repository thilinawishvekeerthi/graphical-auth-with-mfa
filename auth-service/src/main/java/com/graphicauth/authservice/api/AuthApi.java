package com.graphicauth.authservice.api;

import com.graphicauth.authservice.dto.SignUpRequest;
import com.graphicauth.authservice.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthApi {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignUpRequest request) throws QrGenerationException {
        URI location = URI.create( ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/sign-up").toUriString());
        return ResponseEntity.created(location).body(userService.signUpUser(request));
    }
}
