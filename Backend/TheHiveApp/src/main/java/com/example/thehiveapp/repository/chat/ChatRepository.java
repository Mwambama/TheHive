package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByEmployer(Employer employer);
    List<Chat> findAllByStudent(Student student);
}
