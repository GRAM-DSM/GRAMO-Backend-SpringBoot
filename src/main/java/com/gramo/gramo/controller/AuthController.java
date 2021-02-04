package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.AccessTokenResponse;
import com.gramo.gramo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PutMapping
    public AccessTokenResponse tokenRefresh(@RequestHeader("X-Refresh-Token") String refreshToken) { // 토큰 값 가져오기
        return authService.tokenRefresh(refreshToken);      // refresh 토큰으로 access 토큰 재발급 및 refresh 토큰 기간 초기화
    }

}
