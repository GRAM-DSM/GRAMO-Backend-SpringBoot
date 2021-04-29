package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;
import com.gramo.gramo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserInfoResponse getUserInfo() {
        return userService.getUserInfo();
    }

    @GetMapping("/list")
    public UserInfoListResponse getUserList() {
        return userService.getUserList();
    }

}
