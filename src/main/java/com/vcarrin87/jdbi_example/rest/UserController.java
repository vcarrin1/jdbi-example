package com.vcarrin87.jdbi_example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vcarrin87.jdbi_example.models.CustomUser;
import com.vcarrin87.jdbi_example.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint to register a new user with password encoder using BCryptPasswordEncoder.
     * @param user The user details from the request body.
     * @return ResponseEntity with appropriate status and message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CustomUser user) {
        try {
                String hashPwd = passwordEncoder.encode(user.getPassword());
                log.info("Registering user: {}, hashed password: {}", user.getUsername(), hashPwd);
                user.setPassword(hashPwd);
                CustomUser savedUser = userRepository.save(user);
                log.info("User registered with ID: {}", savedUser.getId());

                if (savedUser.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).
                            body("Given user details are successfully registered");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                            body("User registration failed");
                }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }

    }

}
