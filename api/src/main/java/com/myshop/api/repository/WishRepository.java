package com.myshop.api.repository;

import com.myshop.api.domain.entity.Customer;
import com.myshop.api.domain.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByCustomerAndItemId(Customer customer, Long itemId);
}
