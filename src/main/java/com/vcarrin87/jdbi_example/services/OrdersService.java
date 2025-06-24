package com.vcarrin87.jdbi_example.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.OrderItems;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private Jdbi jdbi;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PaymentsService paymentsService;

    /*
     * Get all orders
     * @return a list of Orders
     */
    public List<Orders> getAllOrders() {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_ORDERS)
                .mapTo(Orders.class)
                .list()
        );
    }

    /*
     * Create a new order
     * @param order the order to create
     */
    public void createOrder(Orders order) {
        System.out.println("Creating order: " + order);
        jdbi.useHandle(handle ->
            handle.createUpdate(SqlConstants.INSERT_ORDER)
                  .bind("customer_id", order.getCustomerId())
                  .bind("order_status", order.getOrderStatus())
                  .bind("delivery_date", order.getDeliveryDate())
                  .executeAndReturnGeneratedKeys("order_id")
                  .mapTo(Integer.class)
                  .findFirst()
                  .ifPresent(id -> order.setOrderId(id))
        );
    }

    /*
     * Get all order items
     * @return a list of OrderItems
     */
    public List<OrderItems> getOrderItems() {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_ORDER_ITEMS)
                  .mapTo(OrderItems.class)
                  .list()
        );
    }

    /*
     * Update an order
     * @param order the order to update
     */
    public void updateOrder(Orders order) {
        jdbi.useHandle(handle ->
            handle.createUpdate(SqlConstants.UPDATE_ORDER)
                  .bind("order_id", order.getOrderId())
                  .bind("customer_id", order.getCustomerId())
                  .bind("order_status", order.getOrderStatus())
                  .bind("delivery_date", order.getDeliveryDate())
                  .execute()
        );
    }

    /*
     *  Delete an order by ID
     *  @param orderId the ID of the order to delete
     */
    public void deleteOrder(int orderId) {
        jdbi.useTransaction(handle -> {
            handle.createUpdate(SqlConstants.DELETE_ORDER_ITEMS_BY_ORDER_ID)
              .bind("order_id", orderId)
              .execute();
              
            handle.createUpdate(SqlConstants.DELETE_PAYMENTS_BY_ORDER_ID)
              .bind("order_id", orderId)
              .execute();

            int rowsDeleted = handle.createUpdate(SqlConstants.DELETE_ORDER)
              .bind("order_id", orderId)
              .execute();

            if (rowsDeleted == 0) {
                throw new IllegalStateException("Order not found, rolling back");
            }

            log.info("Order with ID {} deleted successfully", orderId);
        });
    }

    /**
     * Place an order with order items update inventory and create payment
     */
    public void placeOrder(Orders order, List<OrderItems> orderItems) {
        jdbi.useTransaction(handle -> {
            // 1. Create the order
            handle.createUpdate(SqlConstants.INSERT_ORDER)
                .bind("customer_id", order.getCustomerId())
                .bind("order_status", order.getOrderStatus())
                .bind("delivery_date", order.getDeliveryDate())
                .executeAndReturnGeneratedKeys("order_id")
                .mapTo(Integer.class)
                .findFirst()
                .ifPresent(order::setOrderId);

            double totalAmount = 0;
            // 2. Insert order items
            for (OrderItems item : orderItems) {
                log.info("Processing order item: {}", item);
                int productId = item.getProductId();
                int quantity = item.getQuantity();
                double price = item.getPrice();
                handle.createUpdate(SqlConstants.INSERT_ORDER_ITEM)
                    .bind("order_id", order.getOrderId())
                    .bind("product_id", productId)
                    .bind("quantity", quantity)
                    .bind("price", price * quantity)
                    .execute();
                totalAmount += price * quantity;
                // Update inventory
                inventoryService.updateInventory(productId, -quantity);
            }

            // Create payment
            Payments payment = new Payments();
            payment.setOrderId(order.getOrderId());
            payment.setAmount(totalAmount);
            Timestamp paymentDate = new Timestamp(System.currentTimeMillis());
            payment.setPaymentDate(paymentDate);
            payment.setPaymentMethod("Credit Card"); // Assuming a default payment method for simplicity
            log.info("Creating payment for order ID {} with total amount {}", order.getOrderId(), totalAmount);
            paymentsService.createPayment(payment);
        });
    }
}
