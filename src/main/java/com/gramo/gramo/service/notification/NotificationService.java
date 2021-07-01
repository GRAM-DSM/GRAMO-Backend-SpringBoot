package com.gramo.gramo.service.notification;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.payload.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    void sendNotification(User user, String msg);
    void sendMultipleUser(List<User> users, String message);
}
