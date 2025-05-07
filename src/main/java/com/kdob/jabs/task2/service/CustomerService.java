package com.kdob.jabs.task2.service;

import com.kdob.jabs.task2.dao.CustomerDao;
import com.kdob.jabs.task2.exception.CustomerNotFoundException;
import com.kdob.jabs.task2.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDao createCustomer(final CustomerDao customer) {
        return customerRepository.save(customer);
    }

    public CustomerDao getCustomer(final Long id) {
        return findCustomerById(id);
    }

    private CustomerDao findCustomerById(final Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(Long.toString(id)));
    }

    public CustomerDao updateCustomer(final CustomerDao dao, final Long id) {
        final CustomerDao customerDao = findCustomerById(id);
        customerDao.setFirstName(dao.getFirstName());
        customerDao.setLastName(dao.getLastName());
        customerDao.setEmail(dao.getEmail());
        customerDao.setAge(dao.getAge());
        customerRepository.save(customerDao);
        return customerDao;
    }

    public void deleteCustomer(final Long id) {
        customerRepository.deleteById(id);
    }
}
