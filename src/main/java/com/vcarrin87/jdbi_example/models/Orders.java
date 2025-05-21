package com.vcarrin87.jdbi_example.models;

import java.util.Collection;
import java.util.Date;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Orders {

    @JdbiConstructor
    public Orders(int orderId, int customerId, String orderStatus, Date deliveryDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
    }
    
    private int orderId;

    private int customerId;

    private String orderStatus;

    @Nullable
    private Date deliveryDate; 

    @Nullable
    private Collection<Payments> payments;
}
