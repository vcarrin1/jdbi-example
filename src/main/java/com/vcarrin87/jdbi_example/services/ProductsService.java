package com.vcarrin87.jdbi_example.services;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.Inventory;
import com.vcarrin87.jdbi_example.models.OrderItems;
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
        jdbi.useTransaction(handle -> {
            handle.createUpdate(SqlConstants.DELETE_ORDER_ITEMS_BY_PRODUCT_ID)
                .bind("product_id", id)
                .execute();

            // Delete the product itself
            int rowsDeleted = handle.createUpdate(SqlConstants.DELETE_PRODUCT)
                .bind("product_id", id)
                .execute();

            if (rowsDeleted == 0) {
                throw new IllegalStateException("Product not found, rolling back");
            }

            log.info("Product with ID {} deleted successfully", id);
        });
    }

    /**
     * This method retrieves all products that are in stock.
     * @return List of Products that are in stock.
     */
    public List<Products> getProductsInStock() {
        return jdbi.withHandle(handle ->
            handle.createQuery(SqlConstants.SELECT_PRODUCTS_IN_STOCK)
                  .registerRowMapper(ConstructorMapper.factory(Products.class, "p"))       
                  .registerRowMapper(ConstructorMapper.factory(Inventory.class, "i")) 
                  .mapTo(Products.class)
                  .list()
        );
    }

    /**
     * This method retrieves all products along with their associated order items.
     * @return List of Products with their order items populated.
     */
    public List<Products> getProductsWithOrderItems() {

        Multimap<Products, OrderItems> joined = HashMultimap.create();

        jdbi.useHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_PRODUCTS_WITH_ORDER_ITEMS)
                .registerRowMapper(ConstructorMapper.factory(Products.class, "p"))
                .registerRowMapper(ConstructorMapper.factory(OrderItems.class, "oi"))
                .map((rs, ctx) -> {
                    Products product = ctx.findRowMapperFor(Products.class).orElseThrow().map(rs, ctx);
                    OrderItems item = ctx.findRowMapperFor(OrderItems.class).orElseThrow().map(rs, ctx);
                    
                    if (item != null) {
                        joined.put(product, item);
                    }
                    return product;
                })
                .list()
        );

        // Convert the Multimap to a List of Products with their order items
        // Each product will have its order items set
        // This is done by iterating over the keys of the Multimap and setting the order items for each product
        return joined.keySet().stream()
            .map(product -> {
                product.setOrderItems(joined.get(product));
                return product;
            })
            .toList();
    }
}
