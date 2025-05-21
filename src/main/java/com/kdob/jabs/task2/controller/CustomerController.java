package com.kdob.jabs.task2.controller;

import com.kdob.jabs.task2.dto.*;
import com.kdob.jabs.task2.facade.CustomerFacade;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerFacade customerFacade;
    private final Counter createCustomerCounter;

    public CustomerController(final CustomerFacade customerFacade,
                              final MeterRegistry meterRegistry) {
        this.customerFacade = customerFacade;
        createCustomerCounter = Counter.builder("create_customer_counter")
                .description("Number of 'Create customer' requests")
                .register(meterRegistry);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CreateCustomerResponse createCustomer(@RequestBody final CreateCustomerRequest request) {
        createCustomerCounter.increment();
        return customerFacade.createCustomer(request);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public GetCustomerResponse getCustomer(@PathVariable final Long id) {
        return customerFacade.getCustomer(id);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UpdateCustomerResponse updateCustomer(@RequestBody final UpdateCustomerRequest request, @PathVariable final Long id) {
        return customerFacade.updateCustomer(request, id);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable final Long id) {
        customerFacade.deleteCustomer(id);
    }

}
