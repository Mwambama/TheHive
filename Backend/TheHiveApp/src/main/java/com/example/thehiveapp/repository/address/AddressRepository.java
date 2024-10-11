package com.example.thehiveapp.repository.address;

import com.example.thehiveapp.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
