package com.gramo.gramo.service.notification;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.service.notification.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    void sendNotification(User user, NotificationType data, String content);

    void sendMultipleUser(List<User> users, NotificationType data, String content);
}
