package at.fhtw.webshop.controller;

import at.fhtw.webshop.exception.UserNotFoundException;
import at.fhtw.webshop.model.Customer;
import at.fhtw.webshop.model.User;
import at.fhtw.webshop.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    CustomerRepository customerRepository;

    @GetMapping
    ("/customer")
    List<Customer> getCustomer() {
        return customerRepository.findAll();
    }
}
