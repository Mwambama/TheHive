package com.example.thehiveapp.service.address;

import com.example.thehiveapp.entity.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAddresses();
    Address createAddress(Address request);
    Address getAddressById(Long id);
    Address updateAddress(Address request);
    void deleteAddress(Long id);
}
