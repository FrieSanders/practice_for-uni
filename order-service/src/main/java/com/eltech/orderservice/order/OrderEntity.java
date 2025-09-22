package com.eltech.orderservice.order;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private Long userId;

    @Column(nullable=false)
    private Long productId;

    @Column(nullable=false)
    private int quantity;

    @Column(nullable=false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable=false) private String status = "PENDING";
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status=s; }

    public OrderEntity() {}
    public OrderEntity(Long userId, Long productId, int quantity){
        this.userId=userId; this.productId=productId; this.quantity=quantity;
    }

    public Long getId(){ return id; }
    public Long getUserId(){ return userId; }
    public void setUserId(Long v){ this.userId=v; }
    public Long getProductId(){ return productId; }
    public void setProductId(Long v){ this.productId=v; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int v){ this.quantity=v; }
    public OffsetDateTime getCreatedAt(){ return createdAt; }
    public void setCreatedAt(OffsetDateTime v){ this.createdAt=v; }
}
