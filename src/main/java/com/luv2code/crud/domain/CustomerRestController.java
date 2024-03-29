package com.luv2code.crud.domain;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
public class CustomerRestController {

    private static final String ONE_OR_MANY_DIGITS = ".*\\d.*";
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers().stream()
                .map(CustomerDTO::fromCustomer)
                .toList();
    }

    @GetMapping("/customers/{customerId}")
    public CustomerDTO getCustomer(@PathVariable int customerId) {
        return CustomerDTO.fromCustomer(customerService.getCustomer(customerId));
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody Customer theCustomer) {
        Assert.state(isValidFullName(theCustomer), "The name must not contain digits.");
        customerService.saveCustomer(theCustomer);
        return CustomerDTO.fromCustomer(theCustomer);
    }

    private boolean isValidFullName(Customer theCustomer) {
        return !containsDigits(theCustomer);
    }

    @PutMapping("/customers")
    public CustomerDTO updateCustomer(@RequestBody Customer theCustomer) {
        customerService.saveCustomer(theCustomer);
        return CustomerDTO.fromCustomer(theCustomer);
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        customerService.deleteCustomer(customerId);
        return "Delete customer ID " + customerId;
    }

    private boolean containsDigits(Customer theCustomer) {
        String fullName = String.join(" ", List.of(theCustomer.getFirstName(), theCustomer.getLastName()));
        return fullName.matches(ONE_OR_MANY_DIGITS);
    }

}
