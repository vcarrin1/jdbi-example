package com.vcarrin87.jdbi_example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.services.PaymentsService;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentsTests {

    @Autowired
    private PaymentsService paymentsService;

    @Test
    void testGetAllPayments() {
        List<Payments> payments = paymentsService.getAllPayments();

        System.out.println("Payments: " + payments);

        assertEquals(2, payments.size());
        assertEquals(1, payments.get(0).getPaymentId());
        assertEquals(2, payments.get(1).getPaymentId());
    }
}
