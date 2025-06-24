package com.vcarrin87.jdbi_example.services;

import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcarrin87.jdbi_example.constants.SqlConstants;
import com.vcarrin87.jdbi_example.models.Payments;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentsService {

    @Autowired
    private Jdbi jdbi;

    /**
     * This method retrieves all payments.
     * @return List of all Payments.
     */
    public List<Payments> getAllPayments() {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_ALL_PAYMENTS)
                .registerRowMapper(ConstructorMapper.factory(Payments.class))
                .mapTo(Payments.class)
                .list()
        );
    }

    /**
     * This method creates a new payment.
     * @param payment The payment to create.
     */
    public void createPayment(Payments payment) {
        System.out.println("Creating payment: " + payment);
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.INSERT_PAYMENT)
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

    /**
     * This method retrieves a payment by its ID.
     * @param id The ID of the payment to retrieve.
     * @return The payment with the specified ID, or null if not found.
     */
    public Payments getPaymentById(int id) {
        return jdbi.withHandle(handle -> 
            handle.createQuery(SqlConstants.SELECT_PAYMENT_BY_ID)
                .bind("payment_id", id)
                .mapTo(Payments.class)
                .findFirst()
                .orElse(null)
        );
    }

    /**
     * This method updates an existing payment.
     * @param payment The payment to update.
     */
    public void updatePayment(Payments payment) {
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.UPDATE_PAYMENT)
                .bind("payment_id", payment.getPaymentId())
                .bind("order_id", payment.getOrderId())
                .bind("payment_date", payment.getPaymentDate())
                .bind("amount", payment.getAmount())
                .bind("payment_method", payment.getPaymentMethod())
                .execute()
        );
    }

    /**
     * This method deletes a payment by its ID.
     * @param id The ID of the payment to delete.
     */
    public void deletePayment(int id) {
        jdbi.useHandle(handle -> 
            handle.createUpdate(SqlConstants.DELETE_PAYMENT)
                .bind("payment_id", id)
                .execute()
        );

        log.info("Payment with ID {} deleted successfully.", id);
    }
}
