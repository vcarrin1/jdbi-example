package com.vcarrin87.jdbi_example.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.OrderItems;

@Service
public class OrderItemsService {

    @Autowired
    private Jdbi jdbi;

    /**
     * This method retrieves all order items.
     * @return List of all OrderItems.
     */
    public void createOrderItem(OrderItems orderItem) {
        jdbi.useHandle(handle -> {
            handle.createUpdate(SqlConstants.INSERT_ORDER_ITEM)
                .bind("order_id", orderItem.getOrderId())
                .bind("product_id", orderItem.getProductId())
                .bind("quantity", orderItem.getQuantity())
                .bind("price", orderItem.getPrice())
                .executeAndReturnGeneratedKeys("orderitem_id")
                .mapTo(Integer.class)
                .findFirst()
                .ifPresent(orderItem::setOrderItemId);
        });
        
    }

}
