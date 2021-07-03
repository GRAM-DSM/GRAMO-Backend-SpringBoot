package com.gramo.gramo.service.notification;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.payload.request.NotificationRequest;
import com.gramo.gramo.service.notification.enums.NotificationData;

import java.util.List;

public interface NotificationService {
    void sendNotification(User user, NotificationData data);

    void sendMultipleUser(List<User> users, NotificationData data);
}
