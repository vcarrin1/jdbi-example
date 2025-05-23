package com.vcarrin87.jdbi_example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.services.OrdersService;

@SpringBootTest
@ActiveProfiles("test")
public class OrdersTests {

	@Autowired
	private OrdersService ordersService;

    @Order(1)
    @Test
    void testGetAllOrders() {
        // Test logic for getting all orders
        List<Orders> orders = ordersService.getAllOrders();

        System.out.println("Orders: " + orders);

        assertEquals(2, orders.size());
        assertEquals(1, orders.get(0).getOrderId());
        assertEquals(2, orders.get(1).getOrderId());
    }

    @Order(2)
    @Test
    void testGetOrderItems() {
        // Test logic for getting order items
        List<OrderItems> orders = ordersService.getOrderItems();

        System.out.println("Order Items: " + orders);

        assertEquals(2, orders.size());
        assertEquals(1, orders.get(0).getOrderId());
        assertEquals(2, orders.get(1).getOrderId());
    }

    @Order(3)
    @Test
    void testCreateOrder() {
        // Test logic for creating an order
        Orders newOrder = new Orders();
        newOrder.setCustomerId(1);
        newOrder.setOrderStatus("PENDING");
        newOrder.setDeliveryDate(new Date());

        ordersService.createOrder(newOrder);

        List<Orders> orders = ordersService.getAllOrders();

        System.out.println("Orders after creation: " + orders);

        assertEquals(3, orders.size());
        ordersService.deleteOrder(newOrder.getOrderId());
    }
}
