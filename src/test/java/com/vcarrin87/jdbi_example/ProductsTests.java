package com.vcarrin87.jdbi_example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vcarrin87.jdbi_example.models.Products;
import com.vcarrin87.jdbi_example.services.ProductsService;

@SpringBootTest
@ActiveProfiles("test")
public class ProductsTests {

    @Autowired
    private ProductsService productsService;

    @Order(1)
    @Test
    void testGetAllProducts() {
        List<Products> products = productsService.getAllProducts();

        System.out.println("Products: " + products);

        assertEquals(2, products.size());
        assertEquals("Widget", products.get(0).getName());
        assertEquals("Gadget", products.get(1).getName());
    }

    @Order(2)
    @Test
    void testGetProductById() { 
        Products product = productsService.getProductById(1);

        System.out.println("Product: " + product);

        assertEquals("Widget", product.getName());
        assertEquals("A useful widget", product.getDescription());
        assertEquals(9.99, product.getPrice());
    }

    @Order(3)
    @Test
    void testCreateProduct() {
        Products newProduct = new Products();
        newProduct.setName("New Product");
        newProduct.setDescription("A brand new product");
        newProduct.setPrice(19.99);

        productsService.createProduct(newProduct);

        List<Products> products = productsService.getAllProducts();

        assertEquals(3, products.size());
        assertEquals("New Product", products.get(2).getName());
        productsService.deleteProduct(newProduct.getProductId());
    }
}
