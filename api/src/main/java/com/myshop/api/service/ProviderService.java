package com.myshop.api.service;

import com.myshop.api.dto.provider.ProviderUpdateParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ProviderService {

    UserDetails getProviderByUserId(String userId) throws UsernameNotFoundException;
    Boolean checkUserId(String userId);
    Boolean checkBrandName(String brandName);
    Boolean modify(ProviderUpdateParam updateParam);
    Boolean withdrawal(String userId, String password);

}
