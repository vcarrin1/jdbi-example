package com.vcarrin87.jdbi_example.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.services.PaymentsService;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @GetMapping("/")
    public List<Payments> getAllPayments() {
        return paymentsService.getAllPayments();
    }

    /*
     * This method is used to create a new payment.
     * Example of a POST request:
     {
         "orderId": 1,
         "paymentDate": "2025-05-20T03:44:55.123Z",
         "amount": 100.00,
         "paymentMethod": "Credit_Card"
     }
     */
    @PostMapping("/new-payment")        
    public ResponseEntity<?> createPayment(@RequestBody Payments payment) {
        try {
            paymentsService.createPayment(payment);
            return ResponseEntity.ok("Payment created successfully");
        } catch (Exception e) {
            System.out.println("Error creating payment: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating payment: " + e.getMessage());
        }
    }
}
