package com.vcarrin87.jdbi_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
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
    public ResponseEntity<?> getCustomersWithOrders(@RequestParam int customerId) {
        List<Customer> customers = customerService.getCustomersWithOrders(customerId);
        if (customers.isEmpty()) {
            return ResponseEntity.status(404).body("Customer not found");
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customer-with-orders-and-payments")
    public ResponseEntity<?> getCustomersWithOrdersAndPayments(@RequestParam int customerId) {
        Customer customer = customerService.getCustomersWithOrdersAndPayments(customerId);
        if (customer == null) {
            return ResponseEntity.status(404).body("Customer not found");
        }
        return ResponseEntity.ok(customer);
    }
    
    /*
     * This method is used to get a customer by its ID.
     * Example of a GET request:
     /customers/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@RequestParam int id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This method is used to update a customer.
     * Example of a POST request:
     {
        "customerId": 1,
        "name": "Alice Smith",
        "email": "
    }
     */
    @PostMapping("/update-customer")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer) {
        try {
            customerService.updateCustomer(customer);
            return ResponseEntity.ok("Customer updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
            return ResponseEntity.status(500).body("Error updating customer: " + e.getMessage());
        }
    }

    /**
     * This method is used to delete a customer by its ID using Transaction.
     * Example of a DELETE request:
     /customers/delete-customer/1
     */
    @DeleteMapping("/delete-customer/{customerId}")  
    public ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            return ResponseEntity.status(500).body("Error deleting customer: " + e.getMessage());
        }
    }
}
