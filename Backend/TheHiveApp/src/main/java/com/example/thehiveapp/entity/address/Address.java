package com.example.thehiveapp.entity.address;

import jakarta.persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotNull
    @Column(name = "street")
    private String street;

    @Column(name = "complement")
    private String complement;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Size(min = 2, max = 2, message = "Enter state as a two-letter code")
    @Column(name = "state")
    private String state;

    @NotNull
    @Size(min = 5, max = 5, message = "Enter zip as a five-digit code")
    @Column(name = "zip_code", nullable = false)
    private String zipCode;
}

