package com.gramo.gramo.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.SendNotificationFailed;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.service.notification.enums.NotificationData;
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

    private final UserFactory userFactory;

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
    public void sendMultipleUser(List<User> users, NotificationData data) {
        var fcm = MulticastMessage.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .putData("title", "Gramo Notification")
                        .putData("content", data.getMessage())
                        .build())
                .addAllTokens(users.stream().filter(user -> !userFactory.getAuthUser().equals(user)).map(User::getToken).collect(Collectors.toList()))
                .build();
        FirebaseMessaging.getInstance().sendMulticastAsync(fcm);
    }

    @Override
    public void sendNotification(User user, NotificationData data) {
        try {
            Message message = Message.builder()
                    .setToken(user.getToken())
                    .setAndroidConfig(AndroidConfig.builder()
                            .putData("title", "Gramo Notification")
                            .putData("content", data.getMessage())
                            .build())
                    .build();
            FirebaseMessaging.getInstance().sendAsync(message);
        } catch (Exception e) {
            throw new SendNotificationFailed();
        }
    }

}
