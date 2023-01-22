package com.myshop.api.service;

import com.myshop.api.domain.dto.account.ProviderAccount;
import com.myshop.api.domain.dto.request.UserUpdateRequest;
import com.myshop.api.domain.dto.response.data.ProviderData;
import com.myshop.api.domain.entity.Provider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ProviderService {

    UserDetails getProviderByUserId(String userId) throws UsernameNotFoundException;
    ProviderData.Provider getInfo(Provider provider);
    Boolean checkUserId(String userId);
    Boolean checkBrandName(String brandName);
    Boolean modify(UserUpdateRequest updateParam);
    Boolean withdrawal(String userId, String password);

}
