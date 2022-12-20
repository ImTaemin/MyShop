package com.myshop.api.repository;

import com.myshop.api.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, String> {

    Optional<Provider> findByLoginId(String loginId);
    Boolean existsByBrandName(String brandName);
}
