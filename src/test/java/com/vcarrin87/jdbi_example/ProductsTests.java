package com.vcarrin87.jdbi_example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Products;
import com.vcarrin87.jdbi_example.services.OrderItemsService;
import com.vcarrin87.jdbi_example.services.OrdersService;
import com.vcarrin87.jdbi_example.services.ProductsService;

@SpringBootTest
@ActiveProfiles("test")
public class ProductsTests {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private OrdersService ordersService;
    

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

        Orders newOrder = new Orders();
        newOrder.setCustomerId(1); // Assuming a customer with ID 1 exists
        newOrder.setOrderStatus("PENDING");
        newOrder.setDeliveryDate(new Date());
        ordersService.createOrder(newOrder);

        List<Products> products = productsService.getAllProducts();

        System.out.println("Products after creation: " + newProduct);
        System.out.println("Order after creation: " + newOrder);
        System.out.println("Products List: " + products);
        
        OrderItems orderItem = new OrderItems();
        orderItem.setProductId(newProduct.getProductId());
        orderItem.setOrderId(newOrder.getOrderId());
        orderItem.setQuantity(10);
        orderItem.setPrice(newProduct.getPrice());
        orderItemsService.createOrderItem(orderItem);

        System.out.println("Order Items after creation: " + orderItem);

        assertEquals(3, products.size());
        assertEquals("New Product", products.get(2).getName());
        productsService.deleteProduct(newProduct.getProductId()); 
    }

    @Order(4)
    @Test
    void testGetProductsWithOrderItems() {
        List<Products> products = productsService.getProductsWithOrderItems();

        System.out.println("Products with Order Items: " + products);

        assertEquals(2, products.size());
        assertEquals("Widget", products.get(0).getName());
        assertEquals("Gadget", products.get(1).getName());
    }
}
