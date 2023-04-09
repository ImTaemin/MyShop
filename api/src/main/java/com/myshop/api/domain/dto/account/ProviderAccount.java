package com.myshop.api.domain.dto.account;

import com.myshop.api.domain.entity.Provider;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class ProviderAccount extends User {

    Provider provider;

    public ProviderAccount(Provider provider) {
        super(provider.getUserId(), provider.getPassword(), provider.getAuthorities());
        this.provider = provider;
    }
}
