package com.myshop.api.repository;

import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Item;
import com.myshop.api.domain.entity.id.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId>, CartRepositoryCustom {
    void deleteByCustomerAndItemIdIn(Customer customer, List<Long> itemIdList);
}
