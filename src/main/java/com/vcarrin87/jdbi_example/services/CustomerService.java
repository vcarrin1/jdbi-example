package com.vcarrin87.jdbi_example.services;

import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.dao.CustomerWithOrdersDao;
import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Orders;

import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private Jdbi jdbi;

    /**
     * This method creates a new customer.
     * @param customer The customer to create.
     */
    public void createCustomer(Customer customer) {
        System.out.println("Creating customer: " + customer);
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.INSERT_CUSTOMER)
                  .bind("name", customer.getName())
                  .bind("email", customer.getEmail())
                  .bind("address", customer.getAddress())
                  .executeAndReturnGeneratedKeys("customer_id")
                  .mapTo(Integer.class)
                  .findFirst()
                  .ifPresent(customer::setCustomerId)
        );
    }

    /**
     * This method retrieves a customer by its ID.
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID, or null if not found.
     */
    public Customer getCustomerById(int id) {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_CUSTOMER_BY_ID)
                  .bind("customer_id", id)
                  .mapTo(Customer.class)
                  .findFirst()
                  .orElse(null)
        );
    }

    /**
     * This method updates a customer.
     * @param customer The customer to update.
     */
    public void updateCustomer(Customer customer) {
        System.out.println("Updating customer: " + customer);
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.UPDATE_CUSTOMER)
                  .bind("customer_id", customer.getCustomerId())
                  .bind("name", customer.getName())
                  .bind("email", customer.getEmail())
                  .bind("address", customer.getAddress())
                  .execute()
        );
    }

    /**
     * This method deletes a customer with orders and payments.
     * useTransaction() automatically handles commit/rollback
     * Any exception triggers full rollback
     * @param customerId The customer id to delete.
     */
    public void deleteCustomer(int customerId) {
        jdbi.useTransaction(handle -> {
            // 1. Delete payments first (deepest dependency)
            handle.createUpdate("DELETE FROM payments WHERE order_id IN " +
                "(SELECT order_id FROM orders WHERE customer_id = :customer_id)")
                .bind("customer_id", customerId)
                .execute();

            // 2. Delete orders
            handle.createUpdate("DELETE FROM orders WHERE customer_id = :customer_id")
                .bind("customer_id", customerId)
                .execute();

            // 3. Finally delete customer
            int rowsDeleted = handle.createUpdate("DELETE FROM customers WHERE customer_id = :customer_id")
                .bind("customer_id", customerId)
                .execute();

            if (rowsDeleted == 0) {
                throw new IllegalStateException("Customer not found, rolling back");
            }
        });
    }


    /**
     * This method retrieves all customers.
     * @return List of all Customers.
     */
    public List<Customer> getAllCustomers() {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_CUSTOMERS)
                .mapTo(Customer.class)
                .list()
        );
    }

    /**
     * This method retrieves all customers with their orders.
     * @param customerId The ID of the customer to retrieve.
     * @return List of Customers with their Orders.
     */
    public List<Customer> getCustomersWithOrders(int customerId) {
        Multimap<Customer, Orders> joined = HashMultimap.create();

        jdbi.useHandle(handle -> 
            handle.attach(CustomerWithOrdersDao.class)
                .getCustomerWithOrders(customerId)
                .forEach(jr -> joined.put(jr.get(Customer.class), jr.get(Orders.class)))
        );

        return joined.keySet().stream()
            .map(customer -> {
                customer.setOrders(joined.get(customer));
                return customer;
            })
            .toList();
    }

    /**
     * This method retrieves a customer with their orders and payments.
     * @param customerId The ID of the customer to retrieve.
     * @return The Customer object containing the customer, orders, and payments data.
     */
    public Customer getCustomersWithOrdersAndPayments(int customerId) {
        return jdbi.onDemand(CustomerWithOrdersDao.class)
                .getCustomerWithOrdersAndPayments(customerId);
    }
}
