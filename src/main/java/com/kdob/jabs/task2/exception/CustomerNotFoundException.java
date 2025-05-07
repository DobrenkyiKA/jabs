package com.kdob.jabs.task2.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with ID: [%s] not found.";

    public CustomerNotFoundException(final String customerId) {
        super(String.format(CUSTOMER_NOT_FOUND_MESSAGE, customerId));
    }
}
