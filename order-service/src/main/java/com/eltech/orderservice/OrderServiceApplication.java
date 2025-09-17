package com.eltech.orderservice;

import com.eltech.orderservice.order.OrderEntity;
import com.eltech.orderservice.order.OrderRepository;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args){ SpringApplication.run(OrderServiceApplication.class, args); }

    @Bean
    CommandLineRunner init(OrderRepository repo){
        return args -> { if(repo.count()==0){ repo.save(new OrderEntity(1L, 1L, 2)); } };
    }
}