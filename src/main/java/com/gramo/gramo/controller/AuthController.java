package com.gramo.gramo.controller;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.gramo.gramo.payload.request.AuthRequest;
import com.gramo.gramo.payload.response.TokenRefreshResponse;
import com.gramo.gramo.payload.response.TokenResponse;
import com.gramo.gramo.service.auth.AuthService;
import com.gramo.gramo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signIn(@RequestBody AuthRequest request) {
        System.out.println(request.getToken());
        return authService.signIn(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenRefreshResponse tokenRefresh(@RequestHeader(name = "Authorization") String refreshToken) {
        return authService.tokenRefresh(refreshToken);
    }
}
