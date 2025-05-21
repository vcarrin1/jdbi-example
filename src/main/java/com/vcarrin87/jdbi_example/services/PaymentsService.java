package com.vcarrin87.jdbi_example.services;

import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vcarrin87.jdbi_example.models.Payments;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentsService {

    @Autowired
    private Jdbi jdbi;

    public List<Payments> getAllPayments() {
        String sql = "SELECT * FROM payments";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                  .mapTo(Payments.class)
                  .list()
        );
    }

    public void createPayment(Payments payment) {
        System.out.println("Creating payment: " + payment);
        String sql = "INSERT INTO payments (order_id, payment_date, amount, payment_method) VALUES (:order_id, :payment_date, :amount, :payment_method)";
        jdbi.useHandle(handle -> 
            handle.createUpdate(sql)
                .bind("order_id", payment.getOrderId())
                .bind("payment_date", payment.getPaymentDate())
                .bind("amount", payment.getAmount())
                .bind("payment_method", payment.getPaymentMethod())
                .executeAndReturnGeneratedKeys("payment_id")
                .mapTo(Integer.class)
                .findFirst()
                .ifPresent(payment::setPaymentId)
        );
    }
}
