package com.myshop.api.repository;

import com.myshop.api.domain.entity.OrderItem;
import com.myshop.api.domain.entity.id.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

}
