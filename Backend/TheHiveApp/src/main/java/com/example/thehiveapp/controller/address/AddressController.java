package com.example.thehiveapp.controller.address;

import com.example.thehiveapp.entity.address.Address;
import com.example.thehiveapp.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

/**
 * Controller for managing addresses.
 */
@RestController
@RequestMapping("/address")
public class AddressController {


    @Autowired private AddressService addressService;

    @Operation(summary = "Get all addresses", description = "Fetches all addresses from the database")
    @GetMapping
    public List<Address> getAddresses() {
        return addressService.getAddresses();
    }

    @Operation(summary = "Create a new address", description = "Creates a new address in the system")
    @PostMapping
    public Address createAddress(@RequestBody Address request) {
        return addressService.createAddress(request);
    }

    @Operation(summary = "Get address by ID", description = "Fetches a specific address by ID")
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable @Parameter(description = "ID of the address to retrieve", required = true) Long id) {
        return addressService.getAddressById(id);
    }

    @Operation(summary = "Update an address", description = "Updates the information of an existing address")
    @PutMapping
    public Address updateAddress(@RequestBody Address request) {
        return addressService.updateAddress(request);
    }

    @Operation(summary = "Delete an address", description = "Deletes an address from the system by ID")
    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable @Parameter(description = "ID of the address to delete", required = true) Long id) {
        addressService.deleteAddress(id);
        return "Address successfully deleted";
    }
}
