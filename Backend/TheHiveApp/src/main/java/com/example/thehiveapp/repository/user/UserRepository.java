package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
