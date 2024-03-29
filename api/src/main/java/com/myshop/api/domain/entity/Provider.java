package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Provider", indexes = {
        @Index(name = "idx_provider_userid", columnList = "userId"),
        @Index(name = "idx_provider_brandname", columnList = "brandName")
})
public class Provider implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    private Long id;

    /**
     * 원래라면 true 여야함 (카카오 페이에서 사용)
     */
//    @Column(unique = true, nullable = false)
    @Column(nullable = false)
    private String cid = "TC0ONETIME";

    @Column(unique = true, nullable = false)
    private String userId;

    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String brandName;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "provider")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    private List<Coupon> coupons = new ArrayList<>();

    @Setter
    @Column
    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.userId;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public Provider(Long id, String cid, String userId, String password, String phone, String brandName, LocalDateTime createDate, List<Item> items, List<Coupon> coupons, List<String> roles) {
        this.id = id;
        this.cid = cid;
        this.userId = userId;
        this.password = password;
        this.phone = phone;
        this.brandName = brandName;
        this.createDate = createDate;
        this.items = items;
        this.coupons = coupons;
        this.roles = roles;
    }

}
