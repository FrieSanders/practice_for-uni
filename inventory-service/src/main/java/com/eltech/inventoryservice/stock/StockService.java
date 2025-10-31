package com.eltech.inventoryservice.stock;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    // in-memory: productId -> остаток
    private final Map<Long, Integer> stock = new ConcurrentHashMap<>();

    public StockService() {
        stock.putIfAbsent(1L, 100);
        stock.putIfAbsent(2L, 100);
    }

    public synchronized void reserve(long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        int cur = stock.getOrDefault(productId, 0);
        if (cur < quantity) throw new IllegalStateException("not enough stock");
        stock.put(productId, cur - quantity);
    }

    public synchronized void release(long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        int cur = stock.getOrDefault(productId, 0);
        stock.put(productId, cur + quantity);
    }

    public int get(long productId) {
        return stock.getOrDefault(productId, 0);
    }
}
