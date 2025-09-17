package com.eltech.notificationservice.notify;

import com.eltech.notificationservice.amqp.RabbitConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderListener {
    @RabbitListener(queues = RabbitConstants.QUEUE)
    public void onOrder(Object payload) {
        System.out.println("Notification received: " + payload);
    }
}
