package com.example.thehiveapp.repository.authentication;

import com.example.thehiveapp.entity.authentication.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
}
