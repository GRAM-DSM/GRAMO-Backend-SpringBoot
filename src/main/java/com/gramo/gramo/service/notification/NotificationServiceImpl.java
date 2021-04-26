package com.gramo.gramo.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import com.google.gson.JsonObject;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.payload.request.NotificationRequest;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    @Async
    public void sendNotification(NotificationRequest request) {
        try {
            Message message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(Notification.builder()
                            .setBody(request.getBody())
                            .setTitle(request.getTitle())
                            .build())
                    .build();
            firebaseMessaging.sendAsync(message).get();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }
}
