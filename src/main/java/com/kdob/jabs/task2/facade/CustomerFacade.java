package com.kdob.jabs.task2.facade;

import com.kdob.jabs.task2.dao.CustomerDao;
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

    public CreateCustomerResponseDto createCustomer(final CreateCustomerRequestDto createCustomerRequestDto) {
        final var customerToCreate = customerMapper.toCustomerDao(createCustomerRequestDto);
        final var createdCustomer = customerService.createCustomer(customerToCreate);
        return customerMapper.toCreateCustomerResponseDto(createdCustomer);
    }

    public GetCustomerResponseDto getCustomer(final Long id) {
        final CustomerDao customer = customerService.getCustomer(id);
        return customerMapper.toGetCustomerResponseDto(customer);
    }

    public UpdateCustomerResponseDto updateCustomer(final UpdateCustomerRequestDto customer, final Long id) {
        final var customerToUpdate = customerMapper.toCustomerDao(customer);
        final var updatedCustomer = customerService.updateCustomer(customerToUpdate, id);
        return customerMapper.toUpdateCustomerResponseDto(updatedCustomer);
    }

    public void deleteCustomer(final Long id) {
        customerService.deleteCustomer(id);
    }
}
