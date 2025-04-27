package com.sin.management_system.infrastructures.utils;

import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.TOP_END);
    }
}
