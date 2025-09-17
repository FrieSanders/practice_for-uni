package com.eltech.orderservice.order;

import com.eltech.orderservice.amqp.RabbitConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final RabbitTemplate rabbit;
    public OrderProducer(RabbitTemplate rabbit){ this.rabbit = rabbit; }

    public void publish(OrderEvent event){
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.ROUTING_KEY, event);
    }
}
