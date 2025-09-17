package com.eltech.inventoryservice;

import com.eltech.inventoryservice.stock.StockItem;
import com.eltech.inventoryservice.stock.StockItemRepository;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args){ SpringApplication.run(InventoryServiceApplication.class, args); }

    @Bean
    CommandLineRunner init(StockItemRepository repo){
        return args -> { if(repo.count()==0){ repo.save(new StockItem(1L, 10)); repo.save(new StockItem(2L, 50)); } };
    }
}

@RestController
class StockController {
    private final StockItemRepository repo;
    StockController(StockItemRepository repo){ this.repo = repo; }

    @GetMapping("/stock") List<StockItem> all(){ return repo.findAll(); }
    @GetMapping("/stock/count") long count(){ return repo.count(); }
}
