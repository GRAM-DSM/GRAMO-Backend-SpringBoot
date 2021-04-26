package com.gramo.gramo.service.notification;

import com.gramo.gramo.payload.request.NotificationRequest;

import java.util.concurrent.ExecutionException;

public interface NotificationService {
    void sendNotification(NotificationRequest request) throws ExecutionException, InterruptedException;
}
