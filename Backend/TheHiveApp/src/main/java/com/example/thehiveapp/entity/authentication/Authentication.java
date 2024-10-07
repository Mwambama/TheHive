package com.example.thehiveapp.entity.authentication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "authentication")
public class Authentication {

    @Id
    @Column(name="user_id")
    private Long userId;

    @NotNull
    @Column(name="password")
    private String password;
}
