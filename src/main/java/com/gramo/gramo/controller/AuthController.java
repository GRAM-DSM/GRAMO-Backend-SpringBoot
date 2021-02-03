package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.AccessTokenResponse;
import com.gramo.gramo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticationTest")
public class AuthController {

    private final AuthService authService;

    @PutMapping
    public AccessTokenResponse tokenRefresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.tokenRefresh(refreshToken);
    }

}
