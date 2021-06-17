package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.EmailRequest;
import com.gramo.gramo.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendemail")
    public void sendEmail(EmailRequest emailRequest) {
        emailService.sendAuthNumEmail(emailRequest);
    }

}
