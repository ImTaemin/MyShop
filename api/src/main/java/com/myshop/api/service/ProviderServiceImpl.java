package com.myshop.api.service;

import com.myshop.api.domain.dto.account.ProviderAccount;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.data.ProviderData;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.exception.UserNotFoundException;
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
    public UserDetails getProviderByUserId(String userId) {
        Provider provider =  providerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        return new ProviderAccount(provider);
    }

    @Override
    public ProviderData.Provider getInfo(Provider provider) {
        if(provider == null) throw new UserNotFoundException();

        return new ProviderData.Provider(provider);
    }

    @Transactional
    @Override
    public Boolean checkUserId(String userId) {
        return !providerRepository.existsByUserId(userId);
    }

    @Transactional
    @Override
    public Boolean checkBrandName(String brandName) {
        return !providerRepository.existsByBrandName(brandName);
    }

    @Transactional
    @Override
    public Boolean modify(UserUpdateRequest updateParam) {
        Provider dbProvider = providerRepository.findByUserId(updateParam.getUserId()).orElseThrow(UserNotFoundException::new);

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
    public Boolean withdrawal(String userId, String password) {
        Provider dbProvider = providerRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        
       if(PasswordEncryptor.isMatchBcrypt(password, dbProvider.getPassword())) {
           providerRepository.delete(dbProvider);

           return true;
        }
        
        LOGGER.info("패스워드 불일치");

        return false;
    }

}
