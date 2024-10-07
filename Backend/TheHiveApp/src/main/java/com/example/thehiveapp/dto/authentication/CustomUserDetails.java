package com.example.thehiveapp.dto.authentication;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.repository.authentication.AuthenticationRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CustomUserDetails implements UserDetails {

    private final User user;
    private final AuthenticationRepository authenticationRepository;

    public CustomUserDetails(User user, AuthenticationRepository authenticationRepository) {
        this.user = user;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(user.getRole())
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return authenticationRepository.findById(user.getUserId()).get().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

