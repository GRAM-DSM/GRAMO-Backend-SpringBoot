package com.gramo.gramo.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.exception.SendNotificationFailed;
import com.gramo.gramo.payload.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String path = "gramo-d19da-firebase-adminsdk-tka95-218dd64be4";

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(path).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMultipleUser(List<User> users, String message) {
        for (User user : users) {
            this.sendNotification(user, message);
        }
    }

    @Override
    public void sendNotification(User user, String msg) {
        try {
            Message message = Message.builder()
                    .setToken(user.getToken())
                    .setNotification(Notification.builder()
                            .setBody(msg)
                            .setTitle("Gramo Notification")
                            .build())
                    .build();
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            throw new SendNotificationFailed();
        }
    }

}
