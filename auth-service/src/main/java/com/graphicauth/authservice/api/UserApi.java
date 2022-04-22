package com.graphicauth.authservice.api;

import com.graphicauth.authservice.dto.ConfigDto;
import com.graphicauth.authservice.dto.UserDto;
import com.graphicauth.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/config/{userName}")
    public ResponseEntity<ConfigDto> getUserConfig(@PathVariable("userName") String userName){
        return ResponseEntity.ok(userService.getUserConfig(userName));
    }

}
