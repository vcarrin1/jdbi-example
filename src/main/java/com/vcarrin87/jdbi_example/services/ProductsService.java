package com.vcarrin87.jdbi_example.services;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.Products;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductsService {

    @Autowired
    private Jdbi jdbi;

    /**
     * This method creates a new product.
     * @param product The product to create.
     */
    public void createProduct(Products product) {
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.INSERT_PRODUCT)
                .bind("name", product.getName())
                .bind("description", product.getDescription())
                .bind("price", product.getPrice())
                .executeAndReturnGeneratedKeys("product_id")
                .mapTo(Integer.class)
                .findFirst()
                .ifPresent(product::setProductId)
        );
    }

    /**
     * This method retrieves all products.
     * @return List of all Products.
     */
    public List<Products> getAllProducts() {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_PRODUCTS)
                  .mapTo(Products.class)
                  .list()
        );
    }

    /**
     * This method retrieves a product by its ID.
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID, or null if not found.
     */
    public Products getProductById(int id) {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_PRODUCT_BY_ID)
                  .bind("product_id", id)
                  .mapTo(Products.class)
                  .findFirst()
                  .orElse(null)
        );
    }

    /**
     * This method updates a product in the database.
     * @param product The product to update.
     */
    public void updateProduct(Products product) {
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.UPDATE_PRODUCT)
                .bind("name", product.getName())
                .bind("description", product.getDescription())
                .bind("price", product.getPrice())
                .bind("product_id", product.getProductId())
                .execute()
        );
    }

    /**
     * This method deletes a product by its ID.
     * @param id The ID of the product to delete.
     */
    public void deleteProduct(int id) {
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.DELETE_PRODUCT)
                .bind("product_id", id)
                .execute()
        );
    }

    /**
     * This method retrieves all products that are in stock.
     * @return List of Products that are in stock.
     */
    public List<Products> getProductsInStock() {
        return jdbi.withHandle(handle ->
            handle.createQuery(SqlConstants.SELECT_PRODUCTS_IN_STOCK)
                  .mapTo(Products.class)
                  .list()
        );
    }
}
