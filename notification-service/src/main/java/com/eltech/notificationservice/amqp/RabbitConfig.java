package com.eltech.notificationservice.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean TopicExchange orderExchange() { return new TopicExchange(RabbitConstants.EXCHANGE, true, false); }
    @Bean Queue orderQueue() { return new Queue(RabbitConstants.QUEUE, true); }
    @Bean Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(RabbitConstants.ROUTING_KEY);
    }
}
