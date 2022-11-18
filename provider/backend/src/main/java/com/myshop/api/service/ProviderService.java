package com.myshop.api.service;

import com.myshop.api.domain.Provider;
import com.myshop.api.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderService {

    @Autowired
    ProviderRepository providerRepository;

}
