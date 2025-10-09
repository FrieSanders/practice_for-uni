package com.eltech.catalogservice;

import com.eltech.catalogservice.product.Product;
import com.eltech.catalogservice.product.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/products")
    public List<Product> all() {
        return repo.findAll();
    }

    @GetMapping("/products/count")
    public long count() {
        return repo.count();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
