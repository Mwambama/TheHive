package com.example.thehiveapp.service.address;

import com.example.thehiveapp.entity.address.Address;
import com.example.thehiveapp.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired private AddressRepository addressRepository;

    public AddressServiceImpl() {}

    @Override
    public List<Address> getAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(Address request) {
        return addressRepository.save(request);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id " + id)
        );
    }

    @Override
    public Address updateAddress(Address request) {
        Long id = request.getAddressId();
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with id " + id);
        }
        return addressRepository.save(request);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id " + id)
        );
        addressRepository.delete(address);
    }
}
