package com.myshop.api.repository;

import com.myshop.api.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Provider save(Provider provider);
}
