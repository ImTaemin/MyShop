package com.myshop.api.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Provider {

    @Id
    @Column(name="PROVIDER_ID", length = 20)
    private String providerId;

    @Column(name="PROVIDER_PW", nullable = false)
    private String providerPw;

    @Column(name="STORE_NAME", unique = true, nullable = false)
    private String storeName;

    @CreationTimestamp
    @Column(name="CREATE_DATE")
    private LocalDateTime createData;

}
