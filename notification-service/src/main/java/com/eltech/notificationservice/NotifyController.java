package com.eltech.notificationservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class NotifyController {

    public static record NotifyReq(Long orderId, String status) {}
    public static record NotifyResp(boolean notified) {}

    @PostMapping("/notify")
    public NotifyResp notify(@RequestBody NotifyReq r) {
        System.out.println("NOTIFY " + r.orderId() + " " + r.status());
        return new NotifyResp(true);
    }
}
