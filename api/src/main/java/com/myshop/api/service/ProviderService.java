package com.myshop.api.service;

import com.myshop.api.dto.provider.ProviderUpdateParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ProviderService {

    UserDetails getProviderByLoginId(String loginId) throws UsernameNotFoundException;
    Boolean checkBrandName(String brandName);
    Boolean modify(ProviderUpdateParam updateParam);
    Boolean withdrawal(String loginId, String password);

}
