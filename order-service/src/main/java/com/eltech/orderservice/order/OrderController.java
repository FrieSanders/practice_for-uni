package com.eltech.orderservice.order;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderFlow flow;
    private final OrderRepository repo;

    public OrderController(OrderFlow flow, OrderRepository repo) {
        this.flow = flow;
        this.repo = repo;
    }

    @PostMapping
    public OrderEntity create(@RequestParam Long userId,
                              @RequestParam Long productId,
                              @RequestParam int quantity) {
        return flow.create(userId, productId, quantity);
    }

    @GetMapping
    public List<OrderEntity> list(@RequestParam(required = false) String status) {
        return (status == null || status.isBlank())
                ? repo.findAllByOrderByIdDesc()
                : repo.findByStatusIgnoreCaseOrderByIdDesc(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
