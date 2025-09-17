package com.eltech.orderservice.order;

public class OrderEvent {
    public Long orderId;
    public Long userId;
    public Long productId;
    public int quantity;

    public OrderEvent() {}
    public OrderEvent(Long orderId, Long userId, Long productId, int quantity){
        this.orderId=orderId; this.userId=userId; this.productId=productId; this.quantity=quantity;
    }
}
