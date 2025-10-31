package com.eltech.orderservice.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OrderFlow {

    private static final Logger log = LoggerFactory.getLogger(OrderFlow.class);

    private final OrderRepository repo;
    private final RestTemplate http;

    // Читаем из ENV (compose задаёт CATALOG_URL и т.п.)
    @Value("${CATALOG_URL:http://catalog-service:8082}")
    private String catalogBase;

    @Value("${INVENTORY_URL:http://inventory-service:8083}")
    private String inventoryBase;

    @Value("${PAYMENT_URL:http://payment-mock:8086}")
    private String paymentBase;

    @Value("${NOTIFY_URL:http://notification-service:8084}")
    private String notifyBase;

    public OrderFlow(OrderRepository repo) {
        this.repo = repo;
        this.http = new RestTemplate();
    }

    @Transactional
    public OrderEntity create(String userId, Long productId, int quantity) {
        log.info("Create order: user={}, productId={}, qty={}", userId, productId, quantity);

        double price = fetchPriceWithFallback(productId);
        double total = price * quantity;

        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setAmount(quantity);
        order.setPrice(price);
        order.setTotal(total);
        order.setStatus("NEW");
        order = repo.save(order);

        boolean reserved = false;
        try {
            reserveWithFallback(productId, quantity);
            reserved = true;
            order.setStatus("RESERVED");
            repo.save(order);

            payWithFallback(userId, total);
            order.setStatus("PAID");
            repo.save(order);

            notifyWithFallback(userId, order.getId(), productId, quantity, total);
            order.setStatus("NOTIFIED");
            return repo.save(order);

        } catch (RuntimeException ex) {
            log.error("Order flow failed, orderId={}: {}", order.getId(), ex.getMessage(), ex);
            order.setStatus("FAILED");
            repo.save(order);
            if (reserved) {
                try { releaseWithFallback(productId, quantity); }
                catch (Exception ignore) { log.warn("Release fallback failed: {}", ignore.getMessage()); }
            }
            throw ex;
        }
    }

    // ----------------- Каталог -----------------
    private double fetchPriceWithFallback(Long productId) {
        List<String> urls = List.of(
                catalogBase + "/products/" + productId,
                catalogBase + "/api/products/" + productId,
                catalogBase + "/products?id=" + productId
        );
        for (String url : urls) {
            try {
                log.debug("GET {}", url);
                ResponseEntity<Map> res = http.getForEntity(url, Map.class);
                if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null && res.getBody().get("price") != null) {
                    Object p = res.getBody().get("price");
                    double price = (p instanceof Number) ? ((Number) p).doubleValue() : Double.parseDouble(String.valueOf(p));
                    log.info("Price fetched: {} via {}", price, url);
                    return price;
                }
                log.warn("Price not in body via {}: {}", url, res.getBody());
            } catch (Exception e) {
                log.warn("Price fetch failed {}: {}", url, e.getMessage());
            }
        }
        throw new IllegalStateException("catalog price not found (all fallbacks failed)");
    }

    // ----------------- Склад -----------------
    private void reserveWithFallback(Long productId, int qty) {
        List<String> urls = List.of(
                inventoryBase + "/stock/reserve?productId=" + productId + "&quantity=" + qty,
                inventoryBase + "/reserve?productId=" + productId + "&quantity=" + qty
        );
        postNoBodyWithFallback("reserve", urls);
    }

    private void releaseWithFallback(Long productId, int qty) {
        List<String> urls = List.of(
                inventoryBase + "/stock/release?productId=" + productId + "&quantity=" + qty,
                inventoryBase + "/release?productId=" + productId + "&quantity=" + qty
        );
        postNoBodyWithFallback("release", urls);
    }

    // ----------------- Оплата -----------------
    private void payWithFallback(String userId, double amount) {
        List<String> urls = List.of(
                paymentBase + "/pay",
                paymentBase + "/api/pay",
                paymentBase + "/payment"
        );
        Map<String, Object> body = Map.of("userId", userId, "amount", amount);
        postJsonWithFallback("payment", urls, body);
    }

    // ----------------- Уведомление -----------------
    private void notifyWithFallback(String userId, Long orderId, Long productId, int qty, double total) {
        List<String> urls = List.of(
                notifyBase + "/notify",
                notifyBase + "/api/notify",
                notifyBase + "/send"
        );
        Map<String, Object> body = Map.of(
                "userId", userId,
                "orderId", orderId,
                "productId", productId,
                "quantity", qty,
                "total", total
        );
        postJsonWithFallback("notify", urls, body);
    }

    // ----------------- Общие помощники -----------------
    private void postNoBodyWithFallback(String tag, List<String> urls) {
        for (String url : urls) {
            try {
                log.debug("POST {} {}", tag, url);
                ResponseEntity<Void> res = http.postForEntity(url, HttpEntity.EMPTY, Void.class);
                if (res.getStatusCode().is2xxSuccessful()) {
                    log.info("{} OK via {}", tag, url);
                    return;
                }
                log.warn("{} non-2xx via {}: {}", tag, url, res.getStatusCode());
            } catch (RestClientException e) {
                log.warn("{} failed {}: {}", tag, url, e.getMessage());
            }
        }
        throw new IllegalStateException(tag + " failed (all fallbacks)");
    }

    private void postJsonWithFallback(String tag, List<String> urls, Map<String, Object> payload) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, h);
        for (String url : urls) {
            try {
                log.debug("POST {} {}", tag, url);
                ResponseEntity<Map> res = http.postForEntity(url, req, Map.class);
                if (res.getStatusCode().is2xxSuccessful()) {
                    log.info("{} OK via {} body={}", tag, url, res.getBody());
                    return;
                }
                log.warn("{} non-2xx via {}: {} body={}", tag, url, res.getStatusCode(), res.getBody());
            } catch (RestClientException e) {
                log.warn("{} failed {}: {}", tag, url, e.getMessage());
            }
        }
        throw new IllegalStateException(tag + " failed (all fallbacks)");
    }
}
