package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.SignUpRequest;
import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;
import com.gramo.gramo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public UserInfoResponse getUserInfo() {
        return userService.getUserInfo();
    }

    @GetMapping("/user/list")
    public UserInfoListResponse getUserList() {
        return userService.getUserList();
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
    }
}
