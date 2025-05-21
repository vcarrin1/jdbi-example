package com.vcarrin87.jdbi_example.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductsService {

    @Autowired
    private Jdbi jdbi;

    public void createProduct(int id, String name, double price) {
        String sql = "INSERT INTO products (id, name, price) VALUES (:id, :name, :price)";
        jdbi.useHandle(handle -> 
            handle.createUpdate(sql)
                  .bind("id", id)
                  .bind("name", name)
                  .bind("price", price)
                  .execute()
        );
    }
}
