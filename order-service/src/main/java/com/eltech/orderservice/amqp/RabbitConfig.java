package com.eltech.orderservice.amqp;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConfig {
    @Bean public TopicExchange orderExchange() { return new TopicExchange(RabbitConstants.EXCHANGE, true, false); }
    @Bean public Queue orderQueue() { return new Queue(RabbitConstants.QUEUE, true); }
    @Bean public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(RabbitConstants.ROUTING_KEY);
    }
}
