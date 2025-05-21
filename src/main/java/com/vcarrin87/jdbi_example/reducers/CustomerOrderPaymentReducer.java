package com.vcarrin87.jdbi_example.reducers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;

/**
 * This class implements RowReducer to aggregate Customer, Orders, and Payments data.
 * It is used to reduce the result set from a SQL query into a nested object graph.
 * When querying for a Customer and all their Orders and Payments in a single query,
 * this reducer will build a Customer object with a list of Orders, each containing a list of Payments.
 * 
 * {
  "customerId": 1,
  "name": "Charlie",
  "email": "charlie@yahoo.com",
  "address": "789 Oak St",
  "orders": [
    {
      "orderId": 1,
      "customerId": 1,
      "orderStatus": "SHIPPED",
      "deliveryDate": "2024-05-01T06:00:00.000+00:00",
      "payments": [
        {
          "paymentId": 1,
          "orderId": 1,
          "amount": 19.98,
          "paymentDate": "2024-05-01T16:00:00.000+00:00",
          "paymentMethod": "CREDIT_CARD"
        }
      ]
    }
  ]
}
 */
public class CustomerOrderPaymentReducer implements RowReducer<Customer, Customer> {
    private Customer customer;
    private final Map<Integer, Orders> orderMap = new HashMap<>();

    @Override
    public Customer container() {
        return new Customer(); // Initial empty container
    }

    @Override
    public void accumulate(Customer container, RowView rowView) {
        // Initialize customer once
        if (customer == null) {
            customer = rowView.getRow(Customer.class);
            customer.setOrders(new ArrayList<>());
        }

        Orders order = rowView.getRow(Orders.class);
        if (order != null && !orderMap.containsKey(order.getOrderId())) {
            order.setPayments(new ArrayList<>());
            customer.getOrders().add(order);
            orderMap.put(order.getOrderId(), order);
        }

        Payments payment = rowView.getRow(Payments.class);
        if (payment != null && order != null) {
            orderMap.get(order.getOrderId()).getPayments().add(payment);
        }
    }

    @Override
    public Stream<Customer> stream(Customer container) {
        return (customer == null) ? Stream.empty() : Stream.of(customer);
    }
}



