package com.vcarrin87.jdbi_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.services.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*
     * This method is used to create a new customer.
     * Example of a POST request:
     {
        "name": "Charlie",
        "email": "charlie@yahoo.com",
        "address": "789 Oak St"
     }
     */
    @PostMapping("/new-customer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            customerService.createCustomer(customer);
            return ResponseEntity.ok("Customer created successfully");
        } catch (Exception e) {
            System.out.println("Error creating customer: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating customer: " + e.getMessage());
        }
    }

    @GetMapping("/all-customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customer-with-orders")
    public List<Customer> getCustomersWithOrders(@RequestParam int customerId) {
        return customerService.getCustomersWithOrders(customerId);
    }

    @GetMapping("/customer-with-orders-and-payments")
    public Customer getCustomersWithOrdersAndPayments(@RequestParam int customerId) {
        return customerService.getCustomersWithOrdersAndPayments(customerId);
    }
}
