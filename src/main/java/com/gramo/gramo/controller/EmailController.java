package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.EmailRequest;
import com.gramo.gramo.payload.request.VerifyRequest;
import com.gramo.gramo.service.email.EmailService;
import com.gramo.gramo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/sendemail")
    @Async
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendAuthNumEmail(emailRequest);
    }

    @PostMapping("/checkcode")
    @Async
    public void verify(@RequestBody VerifyRequest request) {
        userService.verify(request);
    }

}
