package com.eltech.paymentmock;

import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    public record PayReq(Long orderId, long amount) {}
    public record PayResp(boolean authorized) {}

    @PostMapping("/pay")
    public PayResp pay(@RequestBody PayReq r) {
        System.out.println("PAYMENT: approved for order " + r.orderId() + " amount=" + r.amount());
        // всегда успешно
        return new PayResp(true);
    }
}
