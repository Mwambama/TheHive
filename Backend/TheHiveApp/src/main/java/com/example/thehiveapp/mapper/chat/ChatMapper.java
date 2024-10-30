package com.example.thehiveapp.mapper.chat;


import com.example.thehiveapp.dto.chat.ChatDto;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.service.user.EmployerService;
import com.example.thehiveapp.service.user.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmployerService employerService;

    public ChatDto toDto(Chat entity) {
        if (entity == null) {
            return null;
        }
        ChatDto dto = new ChatDto();
        dto.setChatId(entity.getChatId());
        dto.setEmployerId(entity.getEmployer().getUserId());
        dto.setStudentId(entity.getStudent().getUserId());
        return dto;
    }

    public Chat toEntity(ChatDto dto) {
        if (dto == null) {
            return null;
        }
        Chat entity = new Chat();
        entity.setChatId(dto.getChatId());
        entity.setStudent(studentService.getStudentById(dto.getStudentId()));
        entity.setEmployer(employerService.getEmployerById(dto.getEmployerId()));
        return entity;
    }
}

