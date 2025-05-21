package com.vcarrin87.jdbi_example.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Inventory {

    @JdbiConstructor
    public Inventory(int productId, int stockLevel) {
        this.productId = productId;
        this.stockLevel = stockLevel;
    }
  
    private int productId;

    private int stockLevel;
}
