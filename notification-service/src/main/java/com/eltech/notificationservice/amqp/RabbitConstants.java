package com.eltech.notificationservice.amqp;
public final class RabbitConstants {
    private RabbitConstants() {}
    public static final String EXCHANGE = "order-exchange";
    public static final String QUEUE = "order-queue";
    public static final String ROUTING_KEY = "order.created";
}
