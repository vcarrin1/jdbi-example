package com.vcarrin87.jdbi_example.services;

import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private Jdbi jdbi;

    public List<Orders> getAllOrders() {
        String sql = "SELECT * FROM orders";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                  .mapTo(Orders.class)
                  .list()
        );
    }

    public void createOrder(Orders order) {
        System.out.println("Creating order: " + order);
        String sql = "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (:customer_id, :order_status, :delivery_date)";
        jdbi.useHandle(handle ->
            handle.createUpdate(sql)
                  .bind("customer_id", order.getCustomerId())
                  .bind("order_status", order.getOrderStatus())
                  .bind("delivery_date", order.getDeliveryDate())
                  .executeAndReturnGeneratedKeys("order_id")
                  .mapTo(Integer.class)
                  .findFirst()
                  .ifPresent(id -> order.setOrderId(id))
        );
    }

    public List<OrderItems> getOrderItems() {
        String sql = "SELECT * FROM order_items";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                  .mapTo(OrderItems.class)
                  .list()
        );
    }
}
