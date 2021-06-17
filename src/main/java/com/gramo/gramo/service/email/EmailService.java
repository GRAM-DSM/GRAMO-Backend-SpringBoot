package com.gramo.gramo.service.email;

import com.gramo.gramo.payload.request.EmailRequest;

public interface EmailService {
    void sendAuthNumEmail(EmailRequest request);
}
