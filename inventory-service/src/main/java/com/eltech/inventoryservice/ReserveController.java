package com.eltech.inventoryservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class ReserveController {
    record ReserveReq(Long productId, int quantity) {}
    record ReserveResp(boolean reserved) {}

    @PostMapping("/reserve")
    public ReserveResp reserve(@RequestBody ReserveReq r) {
        boolean ok = r.quantity() <= 5; // простое правило
        return new ReserveResp(ok);
    }
}
