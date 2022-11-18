package com.myshop.api.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Provider {

    @Id
    @Column(length = 20)
    private String providerId;

    @Column(nullable = false)
    private String providerPw;

    @Column(unique = true, nullable = false)
    private String storeName;

    @CreationTimestamp
    private LocalDateTime createDate;

}
