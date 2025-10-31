package com.eltech.orderservice;

import com.eltech.orderservice.order.OrderEntity;
import com.eltech.orderservice.order.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    /**
     * Простой сидер. Без старого конструктора.
     * userId теперь String (Firebase UID).
     */
    @Bean
    CommandLineRunner seedOrders(OrderRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // seed #1
                OrderEntity e1 = new OrderEntity();
                e1.setUserId("seed-user-1");   // раньше был long, теперь строка
                e1.setProductId(1L);
                e1.setAmount(2);
                e1.setPrice(1200.0);
                e1.setTotal(e1.getPrice() * e1.getAmount());
                e1.setStatus("NEW");
                repo.save(e1);

                // seed #2
                OrderEntity e2 = new OrderEntity();
                e2.setUserId("seed-user-2");
                e2.setProductId(2L);
                e2.setAmount(1);
                e2.setPrice(25.5);
                e2.setTotal(e2.getPrice() * e2.getAmount());
                e2.setStatus("NEW");
                repo.save(e2);
            }
        };
    }
}
