package com.eltech.inventoryservice.stock;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_items")
public class StockItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private Long productId;

    @Column(nullable=false)
    private int quantity;

    public StockItem() {}
    public StockItem(Long productId, int quantity){ this.productId=productId; this.quantity=quantity; }

    public Long getId(){ return id; }
    public Long getProductId(){ return productId; }
    public void setProductId(Long v){ this.productId=v; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int v){ this.quantity=v; }
}
