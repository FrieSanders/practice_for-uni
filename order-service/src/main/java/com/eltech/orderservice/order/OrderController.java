package com.eltech.orderservice.order;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;
    private final OrderProducer producer;

    public OrderController(OrderRepository repo, OrderProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @GetMapping
    List<OrderEntity> all() {
        return repo.findAll();
    }

    @GetMapping("/count")
    long count() {
        return repo.count();
    }

    // Тестовый POST, который публикует событие в RabbitMQ
    @PostMapping("/test")
    public String publishTest() {
        OrderEvent ev = new OrderEvent(100L, 1L, 1L, 2);
        producer.publish(ev);
        return "published";
    }
}
