package com.myshop.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/*
 * NoArgsConstructor(access = AccessLevel.PROTECTED) : 무분별한 객체 생성에 대해 한번 더 체크
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Customer", indexes = {
        @Index(name = "idx_customer_userid", columnList = "userId")
})

public class Customer implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private List<Orders> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "wish_list",
    joinColumns = @JoinColumn(name = "customer_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> wishList = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private List<Coupon> usedCouponList = new ArrayList<>();

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
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

    /**
     * AllArgsConstructor 는 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성하는데,
     * 인스턴스 멤버의 선언 순서에 영향을 받기 때문에
     * 변수의 순서를 바꾸면 생성자의 입력 값 순서도 바뀌게 되어 검출되지 않는 오류를 발생할 수 있다.
     * 따라서 생성자에 Builder 어노테이션을 작성
     */
    @Builder

    public Customer(Long id, String userId, String password, String phone, String name, LocalDateTime createDate, Set<String> roles, List<Orders> orderList, List<Cart> cartList, Set<Item> wishList, List<Coupon> usedCouponList) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.createDate = createDate;
        this.roles = roles;
        this.orderList = orderList;
        this.cartList = cartList;
        this.wishList = wishList;
        this.usedCouponList = usedCouponList;
    }
}
