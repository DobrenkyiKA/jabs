package com.kdob.jabs.task2.service;

import com.kdob.jabs.task2.dao.Customer;
import com.kdob.jabs.task2.exception.CustomerNotFoundException;
import com.kdob.jabs.task2.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(final Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(final Long id) {
        return findCustomerById(id);
    }

    private Customer findCustomerById(final Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(Long.toString(id)));
    }

    public Customer updateCustomer(final Customer dao, final Long id) {
        final Customer customer = findCustomerById(id);
        customer.setFirstName(dao.getFirstName());
        customer.setLastName(dao.getLastName());
        customer.setEmail(dao.getEmail());
        customer.setAge(dao.getAge());
        customerRepository.save(customer);
        return customer;
    }

    public void deleteCustomer(final Long id) {
        customerRepository.deleteById(id);
    }
}
