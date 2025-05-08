package com.kdob.jabs.task2.mapper;

import com.kdob.jabs.task2.dao.Customer;
import com.kdob.jabs.task2.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CreateCustomerRequest createCustomerRequest);

    CreateCustomerResponse toCreateCustomerResponse(Customer customer);

    GetCustomerResponse toGetCustomerResponse(Customer customer);

    Customer toCustomer(UpdateCustomerRequest updateCustomerRequest);

    UpdateCustomerResponse toUpdateCustomerResponse(Customer customer);
}
