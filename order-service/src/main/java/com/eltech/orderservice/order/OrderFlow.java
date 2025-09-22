package com.eltech.orderservice.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OrderFlow {

    private final RestClient rc;
    private final OrderRepository repo;

    @Value("${INVENTORY_URL:http://inventory-service:8083}")
    String inv;
    @Value("${PAYMENT_URL:http://payment-mock:8086}")
    String pay;
    @Value("${NOTIFY_URL:http://notification-service:8084}")
    String ntf;

    public OrderFlow(RestClient.Builder builder, OrderRepository repo) {
        this.rc = builder.build();
        this.repo = repo;
    }

    record ReserveReq(Long productId, int quantity) {}
    record ReserveResp(boolean reserved) {}
    record PayReq(Long orderId, long amount) {}
    record PayResp(boolean authorized) {}
    record NotifyReq(Long orderId, String status) {}

    public OrderEntity create(Long userId, Long productId, int qty) {
        var o = repo.save(new OrderEntity(userId, productId, qty)); // status=PENDING

        try {
            var r = rc.post().uri(inv + "/reserve")
                    .body(new ReserveReq(productId, qty))
                    .retrieve().body(ReserveResp.class);
            if (r == null || !r.reserved()) {
                o.setStatus("FAILED_INVENTORY");
                return repo.save(o);
            }
        } catch (Exception e) {
            o.setStatus("FAILED_INVENTORY");
            return repo.save(o);
        }

        try {
            var p = rc.post().uri(pay + "/pay")
                    .body(new PayReq(o.getId(), 100))
                    .retrieve().body(PayResp.class);
            if (p == null || !p.authorized()) {
                o.setStatus("FAILED_PAYMENT");
                return repo.save(o);
            }
        } catch (Exception e) {
            o.setStatus("FAILED_PAYMENT");
            return repo.save(o);
        }

        o.setStatus("COMPLETED");
        repo.save(o);
        try {
            rc.post().uri(ntf + "/notify")
                    .body(new NotifyReq(o.getId(), o.getStatus()))
                    .retrieve().toBodilessEntity();
        } catch (Exception ignore) {}
        return o;
    }
}
