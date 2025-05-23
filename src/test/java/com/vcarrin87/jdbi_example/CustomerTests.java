package com.vcarrin87.jdbi_example;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.services.CustomerService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class CustomerTests {

	@Autowired
	private CustomerService customerService;

	@Order(1)
	@Test
	void testGetAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();

		System.out.println("Customers: " + customers);

		assertEquals(2, customers.size());
		assertEquals("Alice", customers.get(0).getName());
		assertEquals("Bob", customers.get(1).getName());
	}

	@Order(2)
	@Test
	void testGetCustomerWithOrders() {
		
		List<Customer> customer = customerService.getCustomersWithOrders(1);

		assertEquals(1, customer.size());
		assertEquals("Alice", customer.get(0).getName());
		assertEquals(1, customer.get(0).getOrders().size());
	}

	@Order(3)
	@Test
	void testGetCustomerWithOrdersAndPayments() {
		Customer customer = customerService.getCustomersWithOrdersAndPayments(1);
		assertEquals("Alice", customer.getName());

		List<Orders> order = customer.getOrders().stream().toList();
		assertEquals(1, customer.getOrders().size());
		assertEquals(1, order.get(0).getPayments().size());

		List<Payments> payments = order.get(0).getPayments().stream().toList();
		assertEquals("CREDIT_CARD", payments.get(0).getPaymentMethod());
	}

	@Order(4)
	@Test
	void testCreateCustomer() {
		Customer newCustomer = new Customer();
		newCustomer.setName("Tom");
		newCustomer.setEmail("tom@gmail.com");
		newCustomer.setAddress("789 Oak St");
		customerService.createCustomer(newCustomer);
		List<Customer> customers = customerService.getAllCustomers();
		System.out.println("Customers after creation: " + customers);
		assertEquals(3, customers.size());
		assertEquals("Tom", customers.get(2).getName());
		customerService.deleteCustomer(newCustomer.getCustomerId());
	}

}
