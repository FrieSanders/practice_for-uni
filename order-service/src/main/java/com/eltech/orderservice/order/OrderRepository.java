package com.eltech.orderservice.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserIdOrderByIdDesc(String userId);

    List<OrderEntity> findByUserIdAndStatusIgnoreCaseOrderByIdDesc(String userId, String status);

    Optional<OrderEntity> findByIdAndUserId(Long id, String userId);
}
