package com.eltech.inventoryservice.stock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    // POST /stock/reserve?productId=1&quantity=2
    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestParam long productId, @RequestParam int quantity) {
        try {
            service.reserve(productId, quantity);
            return ResponseEntity.ok(Map.of(
                    "productId", productId,
                    "reserved", quantity,
                    "remain", service.get(productId)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /stock/release?productId=1&quantity=2
    @PostMapping("/release")
    public ResponseEntity<?> release(@RequestParam long productId, @RequestParam int quantity) {
        try {
            service.release(productId, quantity);
            return ResponseEntity.ok(Map.of(
                    "productId", productId,
                    "released", quantity,
                    "remain", service.get(productId)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /stock?productId=1  (для отладки)
    @GetMapping
    public ResponseEntity<?> get(@RequestParam long productId) {
        return ResponseEntity.ok(Map.of(
                "productId", productId,
                "remain", service.get(productId)
        ));
    }
}
