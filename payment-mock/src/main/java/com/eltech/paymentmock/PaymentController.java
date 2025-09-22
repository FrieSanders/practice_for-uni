package com.eltech.paymentmock;

import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    public static record PayReq(Long orderId, long amount) {}
    public static record PayResp(boolean authorized) {}

    @PostMapping("/pay")
    public PayResp pay(@RequestBody PayReq r) {
        boolean ok = (r.orderId() % 3) != 0; // каждый 3-й — отказ
        return new PayResp(ok);
    }
}
