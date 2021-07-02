package com.gramo.gramo.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.exception.SendNotificationFailed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String path = "gramo-notice-notify-firebase-adminsdk-2njk9-3c4a5af8b9.json";

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(path).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
                System.out.println("Initialized!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMultipleUser(List<User> users, String message) {
        var fcm = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setBody(message)
                        .setTitle("Gramo Notification")
                        .build())
                .addAllTokens(users.stream().map(User::getToken).collect(Collectors.toList()))
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(fcm);
    }

    @Override
    public void sendNotification(User user, String msg) {
        System.out.println(msg);
        System.out.println(user.getToken());

        try {
            Message message = Message.builder()
                    .setToken(user.getToken())
                    .setNotification(Notification.builder()
                            .setBody(msg)
                            .setTitle("Gramo Notification")
                            .build())
                    .build();
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SendNotificationFailed();
        }
    }

}
