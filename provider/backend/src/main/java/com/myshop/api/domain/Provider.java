package com.myshop.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Provider {

    @Id
    @Column(name = "provider_id")
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String brandName;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "provider")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "provider")
    private List<Coupon> coupons = new ArrayList<>();

}
