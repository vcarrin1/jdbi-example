package com.vcarrin87.jdbi_example.services;

import org.springframework.stereotype.Service;
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

    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM customers";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                  .mapTo(Customer.class)
                  .list()
        );
    }

    public void createCustomer(Customer customer) {
        System.out.println("Creating customer: " + customer);
        String sql = "INSERT INTO customers (name, email, address) VALUES (:name, :email, :address)";
        jdbi.useHandle(handle -> 
            handle.createUpdate(sql)
                  .bind("name", customer.getName())
                  .bind("email", customer.getEmail())
                  .bind("address", customer.getAddress())
                  .executeAndReturnGeneratedKeys("customer_id")
                  .mapTo(Integer.class)
                  .findFirst()
                  .ifPresent(customer::setCustomerId)
        );
    }

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

    public Customer getCustomersWithOrdersAndPayments(int customerId) {
        return jdbi.onDemand(CustomerWithOrdersDao.class)
                .getCustomerWithOrdersAndPayments(customerId);
    }
}
