package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
}
