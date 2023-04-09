package com.myshop.api.enumeration.converter;

import com.myshop.api.enumeration.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        return source.isEmpty() ? null : OrderStatus.valueOf(source.toUpperCase());
    }
}
