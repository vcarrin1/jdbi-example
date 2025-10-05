package com.vcarrin87.jdbi_example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vcarrin87.jdbi_example.models.CustomUser;
import com.vcarrin87.jdbi_example.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        CustomUser user = userRepository.findByUsername(username).orElseThrow(() -> new
                UsernameNotFoundException("User details not found for the user: " + username));
        String roles = user.getRoles();
        if (roles == null || roles.trim().isEmpty()) {
            throw new UsernameNotFoundException("User role is not defined for user: " + username);
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roles));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

}