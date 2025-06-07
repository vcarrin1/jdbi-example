package com.vcarrin87.jdbi_example.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItems {
    
    @JdbiConstructor
    public OrderItems(int orderItemId, int orderId, int productId, int quantity, Double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    private int orderItemId;

    private int orderId;

    private int productId;

    private int quantity;

    private Double price;
}
