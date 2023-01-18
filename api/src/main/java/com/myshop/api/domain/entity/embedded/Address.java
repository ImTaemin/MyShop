package com.myshop.api.domain.entity.embedded;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {

    private String loadName;
    private String detail;
    private String postalCode;
}
