package com.eltech.orderservice.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<OrderEntity> create(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String userUid = auth.getName();
        OrderEntity created = flow.create(userUid, productId, quantity);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> list(
            @RequestParam(required = false) String status,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String userUid = auth.getName();
        List<OrderEntity> out = (status == null || status.isBlank())
                ? repo.findByUserIdOrderByIdDesc(userUid)
                : repo.findByUserIdAndStatusIgnoreCaseOrderByIdDesc(userUid, status);
        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> get(
            @PathVariable Long id,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        String userUid = auth.getName();
        return repo.findByIdAndUserId(id, userUid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
