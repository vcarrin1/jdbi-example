package com.vcarrin87.jdbi_example.models;

import java.sql.Timestamp;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payments {

    @JdbiConstructor
    public Payments(int paymentId, int orderId, double amount, Timestamp paymentDate, String paymentMethod) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    private int paymentId;

    private int orderId;

    private double amount;

    @Nullable
    private Timestamp paymentDate;

    @Nullable
    private String paymentMethod;
}
