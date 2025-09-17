package com.eltech.notificationservice;

import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
    @GetMapping("/health") public String ok() { return "OK"; }
}
