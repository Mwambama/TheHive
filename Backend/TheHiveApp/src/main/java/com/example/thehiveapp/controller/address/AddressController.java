package com.example.thehiveapp.controller.address;

import com.example.thehiveapp.entity.address.Address;
import com.example.thehiveapp.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired private AddressService addressService;

    public AddressController() {}

    @GetMapping
    public List<Address> getAddresses() {
        return addressService.getAddresses();
    }

    @PostMapping
    public Address createAddress(@RequestBody Address request) {
        return addressService.createAddress(request);
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @PutMapping
    public Address updateAddress(@RequestBody Address request) {
        return addressService.updateAddress(request);
    }

    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "Address successfully deleted";
    }
}

