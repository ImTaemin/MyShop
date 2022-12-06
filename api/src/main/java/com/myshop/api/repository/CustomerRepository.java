package com.myshop.api.repository;

import com.myshop.api.domain.Customer;
import com.myshop.api.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

}
