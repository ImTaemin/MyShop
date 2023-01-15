package com.myshop.api.repository;

import com.myshop.api.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByUserId(String userId);
    Boolean existsByUserId(String userId);
    Boolean existsByBrandName(String brandName);
}
