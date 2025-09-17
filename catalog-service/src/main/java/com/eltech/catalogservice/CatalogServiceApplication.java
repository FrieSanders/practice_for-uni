package com.eltech.catalogservice;

import com.eltech.catalogservice.product.Product;
import com.eltech.catalogservice.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class CatalogServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Product("Laptop", 1200.0));
                repo.save(new Product("Mouse", 25.5));
            }
        };
    }
}

@RestController
class ProductController {
    private final ProductRepository repo;

    ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/products")
    List<Product> all() {
        return repo.findAll();
    }

    @GetMapping("/products/count")
    long count() {
        return repo.count();
    }
}
