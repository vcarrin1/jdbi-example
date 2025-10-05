package com.vcarrin87.jdbi_example.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomUser {

    @JdbiConstructor
    public CustomUser(int id, String username, String email, String password, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    private int id;
    private String username;
    private String email;
    private String password;
    private String roles;

}
