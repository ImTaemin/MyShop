package com.myshop.api.repository;

import com.myshop.api.domain.entity.Cart;
import com.myshop.api.domain.entity.id.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId>, CartRepositoryCustom {
}
