package com.eltech.orderservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseAdminConfig {
    @PostConstruct
    public void init() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) return;
        String path = System.getenv("FIREBASE_CREDENTIALS");
        if (path == null || path.isBlank()) {
            System.out.println("FIREBASE_CREDENTIALS not set. Firebase Admin not initialized.");
            return;
        }
        try (FileInputStream in = new FileInputStream(path)) {
            FirebaseOptions opts = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in)).build();
            FirebaseApp.initializeApp(opts);
            System.out.println("Firebase Admin initialized");
        }
    }
}
