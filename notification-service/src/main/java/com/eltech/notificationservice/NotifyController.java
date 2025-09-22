package com.eltech.notificationservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class NotifyController {
    record NotifyReq(Long orderId, String status) {}

    @PostMapping("/notify")
    public void notify(@RequestBody NotifyReq r) {
        System.out.println("NOTIFY " + r.orderId() + " " + r.status());
    }
}
