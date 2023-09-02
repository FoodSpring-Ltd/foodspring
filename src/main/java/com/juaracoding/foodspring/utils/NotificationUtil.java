package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 9/2/2023 10:58 AM
@Last Modified 9/2/2023 10:58 AM
Version 1.0
*/

import com.foodspring.utils.LoggingFile;
import com.juaracoding.foodspring.config.NotificationMessage;
import com.juaracoding.foodspring.config.ServicePath;
import com.juaracoding.foodspring.config.WebSocketPath;
import com.juaracoding.foodspring.dto.OrderNotification;
import com.juaracoding.foodspring.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationUtil {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private String[] strExceptionArr = new String[2];

    public NotificationUtil() {
        strExceptionArr[0] = "NotificationUtil";
    }

    public void sendOrderNotificationToAdmin(OrderNotification orderNotification) {
        try {
            simpMessagingTemplate.convertAndSend(WebSocketPath.TOPIC_NEW_ORDER.toString(), orderNotification);
        } catch (Exception e) {
            strExceptionArr[1] = "sendOrderNotificationToAdmin(OrderNotification orderNotification) --LINE 269";
            LoggingFile.exceptionString(strExceptionArr, e, "y", ConstantMessage.NOT_CRITICAL);
        }
    }

    public void sendOrderNotificationToUser(OrderNotification orderNotification) {
        try {
            simpMessagingTemplate.convertAndSendToUser(orderNotification.getCustomerUsername(),
                    WebSocketPath.TOPIC_NOTIFY_USER, orderNotification);
        } catch (Exception e) {
            strExceptionArr[1] = "sendOrderNotificationToUser(OrderNotification orderNotification) --LINE 286";
            LoggingFile.exceptionString(strExceptionArr, e, "y", ConstantMessage.NOT_CRITICAL);
        }
    }

    public void sendOrderPaidNotifToAdmin(String orderId) {
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setMessage(NotificationMessage.NEW_ORDER);
        notification.setStatus(OrderStatus.PAID.toString());
        notification.setTargetURL(ServicePath.ADMIN_ORDER_MANAGEMENT.concat(OrderStatus.PAID.toString()));
        sendOrderNotificationToAdmin(notification);
    }

    public void sendOrderPaidNotifToUser(String orderId, String customerName) {
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setMessage(NotificationMessage.ORDER_PAID);
        notification.setStatus(OrderStatus.PAID.toString());
        notification.setTargetURL(ServicePath.ORDER_PAID);
        notification.setCustomerUsername(customerName);
        sendOrderNotificationToUser(notification);
    }

    public void sendOrderProcessedNotifToUser(String orderId, String adminUsername, String customerName) {
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setMessage(NotificationMessage.ORDER_PROCESSED);
        notification.setAdminId(adminUsername);
        notification.setStatus(OrderStatus.ON_PROCESS.toString());
        notification.setTargetURL(ServicePath.ORDER_ON_PROCESS);
        notification.setCustomerUsername(customerName);
        sendOrderNotificationToUser(notification);
    }

    public void sendOrderCompletedNotifToUser(String orderId, String adminUsername, String customerName) {
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setMessage(NotificationMessage.ORDER_FINISHED);
        notification.setAdminId(adminUsername);
        notification.setStatus(OrderStatus.COMPLETED.toString());
        notification.setTargetURL(ServicePath.ORDER_COMPLETED);
        notification.setCustomerUsername(customerName);
        sendOrderNotificationToUser(notification);
    }

    public void sendOrderCanceledNotifToUser(String orderId, String reason, String customerName) {
        OrderNotification notification = new OrderNotification();
        notification.setOrderId(orderId);
        notification.setMessage(NotificationMessage.ORDER_CANCELED.concat(" ").concat(reason));
        notification.setStatus(OrderStatus.CANCELED.toString());
        notification.setTargetURL(ServicePath.ORDER_CANCELED);
        notification.setCustomerUsername(customerName);
        sendOrderNotificationToUser(notification);
    }


}
