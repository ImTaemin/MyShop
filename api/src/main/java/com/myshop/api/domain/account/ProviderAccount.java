package com.myshop.api.domain.account;

import com.myshop.api.domain.Provider;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class ProviderAccount extends User {

    Provider provider;

    public ProviderAccount(Provider provider) {
        super(provider.getUserId(), provider.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PROVIDER")));
        this.provider = provider;
    }
}
