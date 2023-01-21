package com.myshop.api.annotation.mock;

import com.myshop.api.domain.dto.account.ProviderAccount;
import com.myshop.api.domain.entity.Provider;
import com.myshop.api.enumeration.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class WithMockProviderSecurityContextFactory implements WithSecurityContextFactory<WithMockProvider> {

    @Override
    public SecurityContext createSecurityContext(WithMockProvider annotation) {
        SecurityContext secContext = SecurityContextHolder.createEmptyContext();

        Provider provider = Provider.builder()
                .userId("taemin")
                .password("1234")
                .phone("010-1234-5678")
                .brandName("브랜드명")
                .roles(Set.of(UserRole.PROVIDER.toString()))
                .build();
        ProviderAccount providerAccount = new ProviderAccount(provider);

        Authentication auth = new UsernamePasswordAuthenticationToken(providerAccount, "1234", providerAccount.getProvider().getAuthorities());
        secContext.setAuthentication(auth);

        return secContext;
    }
}
