package com.kdob.jabs.task2.repository;

import com.kdob.jabs.task2.dao.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
