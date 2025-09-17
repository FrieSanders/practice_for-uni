package com.eltech.inventoryservice.stock;

import org.springframework.data.jpa.repository.JpaRepository;
public interface StockItemRepository extends JpaRepository<StockItem, Long> {}
