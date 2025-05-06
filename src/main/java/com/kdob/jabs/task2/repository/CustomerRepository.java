package com.kdob.jabs.task2.repository;

import com.kdob.jabs.task2.dao.CustomerDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerDao, Long> {
}
