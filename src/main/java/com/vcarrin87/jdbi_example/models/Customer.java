package com.vcarrin87.jdbi_example.models;

import java.util.Collection;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

    @JdbiConstructor
    public Customer(int customerId, String name, String email, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    private int customerId;

    private String name;

    private String email;

    @Nullable
    private String address;

    @Nullable
    private Collection<Orders> orders;
}
