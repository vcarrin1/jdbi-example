package com.vcarrin87.jdbi_example.repository;

import java.util.Optional;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbi_example.models.CustomUser;

@Repository
public class UserRepository {

    private final Jdbi jdbi;

    public UserRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<CustomUser> findByUsername(String username) {
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM users WHERE username = :username")
                  .bind("username", username)
                  .mapToBean(CustomUser.class)
                  .findFirst()
        );
    }

    /**
     * Saves a new user through registration endpoint.
     * @param user The user to save.
     * @return The saved user with the generated ID.
     */
    public CustomUser save(CustomUser user) {
        jdbi.withHandle(handle -> {
            Optional<Integer> generatedId = handle.createUpdate("INSERT INTO users (username, password, roles) VALUES (:username, :password, :roles)")
                  .bind("username", user.getUsername())
                  .bind("password", user.getPassword())
                  .bind("roles", user.getRoles())
                  .executeAndReturnGeneratedKeys("id")
                  .mapTo(Integer.class)
                  .findFirst();
            generatedId.ifPresent(user::setId);
            return null;
        });
        return user;
    }
}
