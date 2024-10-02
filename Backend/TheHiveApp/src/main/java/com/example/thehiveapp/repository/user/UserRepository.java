package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmailAndRole(String email, Role role);
    Boolean existsByEmail(String email);
}
