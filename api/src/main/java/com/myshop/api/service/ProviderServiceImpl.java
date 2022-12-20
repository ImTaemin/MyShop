package com.myshop.api.service;

import com.myshop.api.domain.Provider;
import com.myshop.api.dto.provider.ProviderUpdateParam;
import com.myshop.api.exception.NotExistUserException;
import com.myshop.api.repository.ProviderRepository;
import com.myshop.api.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService{
    
    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final ProviderRepository providerRepository;

    @Override
    public UserDetails getProviderByLoginId(String loginId) {
        return providerRepository.findByLoginId(loginId).orElseThrow(NotExistUserException::new);
    }

    @Transactional
    @Override
    public Boolean checkBrandName(String brandName) {
        return !providerRepository.existsByBrandName(brandName);
    }

    @Transactional
    @Override
    public Boolean modify(ProviderUpdateParam updateParam) {
        Provider dbProvider = providerRepository.findByLoginId(updateParam.getLoginId()).orElseThrow(NotExistUserException::new);

        if(PasswordEncryptor.isMatchBcrypt(updateParam.getPassword(), dbProvider.getPassword())){
            dbProvider.setPassword(updateParam.getModifyPassword());
            dbProvider.setPhone(updateParam.getPhone());

            providerRepository.save(dbProvider);
            return true;
        }

        LOGGER.info("패스워드 불일치");

        return false;
    }

    @Transactional
    @Override
    public Boolean withdrawal(String loginId, String password) {
        Provider dbProvider = providerRepository.findByLoginId(loginId).orElseThrow(NotExistUserException::new);
        
       if(PasswordEncryptor.isMatchBcrypt(password, dbProvider.getPassword())) {
           providerRepository.delete(dbProvider);
           return true;
        }
        
        LOGGER.info("패스워드 불일치");

        return false;
    }

}
