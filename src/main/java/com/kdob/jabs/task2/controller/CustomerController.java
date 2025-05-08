package com.kdob.jabs.task2.controller;

import com.kdob.jabs.task2.dto.*;
import com.kdob.jabs.task2.facade.CustomerFacade;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerFacade customerFacade;

    public CustomerController(final CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @PostMapping
    public CreateCustomerResponse createCustomer(@RequestBody final CreateCustomerRequest request) {
        return customerFacade.createCustomer(request);
    }

    @GetMapping(path = "/{id}")
    public GetCustomerResponse getCustomer(@PathVariable final Long id) {
        return customerFacade.getCustomer(id);
    }

    @PutMapping(path = "/{id}")
    public UpdateCustomerResponse updateCustomer(@RequestBody final UpdateCustomerRequest request, @PathVariable final Long id) {
        return customerFacade.updateCustomer(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCustomer(@PathVariable final Long id) {
        customerFacade.deleteCustomer(id);
    }

}
