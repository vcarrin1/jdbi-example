package com.vcarrin87.jdbi_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.services.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /*
     * This method is used to create a new order.
     * Example of a POST request:
    {
        "customerId": 1,
        "orderStatus": "PENDING",
        "deliveryDate": "2025-05-20T03:44:55.123Z"
    }
     */
    @PostMapping("/new-order")
    public ResponseEntity<?> createOrder(@RequestBody Orders order) {
        try {
            ordersService.createOrder(order);
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping("/all-orders")
    public List<Orders> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/order-items")
    public List<OrderItems> getOrderItems() {
        return ordersService.getOrderItems();
    }
}
