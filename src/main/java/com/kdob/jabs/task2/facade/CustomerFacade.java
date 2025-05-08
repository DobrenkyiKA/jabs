package com.kdob.jabs.task2.facade;

import com.kdob.jabs.task2.dao.Customer;
import com.kdob.jabs.task2.dto.*;
import com.kdob.jabs.task2.mapper.CustomerMapper;
import com.kdob.jabs.task2.service.CustomerService;
import org.springframework.stereotype.Component;

@Component
public class CustomerFacade {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    public CustomerFacade(final CustomerService customerService, final CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    public CreateCustomerResponse createCustomer(final CreateCustomerRequest createCustomerRequest) {
        final var customerToCreate = customerMapper.toCustomer(createCustomerRequest);
        final var createdCustomer = customerService.createCustomer(customerToCreate);
        return customerMapper.toCreateCustomerResponse(createdCustomer);
    }

    public GetCustomerResponse getCustomer(final Long id) {
        final Customer customer = customerService.getCustomer(id);
        return customerMapper.toGetCustomerResponse(customer);
    }

    public UpdateCustomerResponse updateCustomer(final UpdateCustomerRequest customer, final Long id) {
        final var customerToUpdate = customerMapper.toCustomer(customer);
        final var updatedCustomer = customerService.updateCustomer(customerToUpdate, id);
        return customerMapper.toUpdateCustomerResponse(updatedCustomer);
    }

    public void deleteCustomer(final Long id) {
        customerService.deleteCustomer(id);
    }
}
