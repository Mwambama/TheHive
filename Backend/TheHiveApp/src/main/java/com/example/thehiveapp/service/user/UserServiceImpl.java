package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired private UserRepository userRepository;

    public UserServiceImpl() {}

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User request) {
        return userRepository.save(request);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + id)
        );
    }

    public User updateUser(User request) {
        Long id = request.getUserId();
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        return userRepository.save(request);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + id)
        );
        userRepository.delete(user);
    }

    public User getUserByEmail(String email){
       return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email " + email)
        );
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.getUserByEmail(email);
    }
}
