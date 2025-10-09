package com.eltech.orderservice.order;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    // старые (оставим для простых вызовов)
    List<OrderEntity> findAllByOrderByIdDesc();
    List<OrderEntity> findByStatusIgnoreCaseOrderByIdDesc(String status);

    // новые — для /orders с page/size и фильтрами
    Page<OrderEntity> findAllByOrderByIdDesc(Pageable p);
    Page<OrderEntity> findByStatusIgnoreCaseOrderByIdDesc(String status, Pageable p);
    Page<OrderEntity> findByUserIdOrderByIdDesc(Long userId, Pageable p);
    Page<OrderEntity> findByUserIdAndStatusIgnoreCaseOrderByIdDesc(Long userId, String status, Pageable p);
}
