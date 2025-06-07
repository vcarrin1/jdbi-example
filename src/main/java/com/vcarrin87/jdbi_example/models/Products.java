package com.vcarrin87.jdbi_example.models;

import java.util.Collection;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Products {
    
    @JdbiConstructor
    public Products(int productId, String name, String description, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private int productId;

    private String name;

    @Nullable
    private String description;

    private Double price;

    @Nullable
    private Collection<OrderItems> orderItems;
}
