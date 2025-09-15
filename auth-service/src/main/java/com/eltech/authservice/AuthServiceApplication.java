package com.eltech.authservice;

import com.eltech.authservice.user.User;
import com.eltech.authservice.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User("test@eltech.local"));
            }
        };
    }
}

@RestController
class UserController {
    private final UserRepository repo;

    UserController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/users")
    List<User> all() {
        return repo.findAll();
    }

    @GetMapping("/users/count")
    long count() {
        return repo.count();
    }
}
