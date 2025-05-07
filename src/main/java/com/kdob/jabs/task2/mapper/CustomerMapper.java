package com.kdob.jabs.task2.mapper;

import com.kdob.jabs.task2.dao.CustomerDao;
import com.kdob.jabs.task2.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDao toCustomerDao(CreateCustomerRequestDto dto);

    CreateCustomerResponseDto toCreateCustomerResponseDto(CustomerDao dao);

    GetCustomerResponseDto toGetCustomerResponseDto(CustomerDao dao);

    CustomerDao toCustomerDao(UpdateCustomerRequestDto dto);

    UpdateCustomerResponseDto toUpdateCustomerResponseDto(CustomerDao dao);
}
