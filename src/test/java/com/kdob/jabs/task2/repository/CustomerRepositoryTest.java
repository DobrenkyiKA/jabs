package com.kdob.jabs.task2.repository;

import com.kdob.jabs.task2.dao.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        // Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAge(30);

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        // Verify the customer was saved with an ID
        assertNotNull(savedCustomer.getId());
        assertEquals("John", savedCustomer.getFirstName());
        assertEquals("Doe", savedCustomer.getLastName());
        assertEquals("john.doe@example.com", savedCustomer.getEmail());
        assertEquals(30, savedCustomer.getAge());
    }

    @Test
    public void testFindCustomerById() {
        // Create and save a customer
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setEmail("jane.smith@example.com");
        customer.setAge(25);
        Customer savedCustomer = customerRepository.save(customer);

        // Find the customer by ID
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

        // Verify the customer was found
        assertTrue(foundCustomer.isPresent());
        assertEquals("Jane", foundCustomer.get().getFirstName());
        assertEquals("Smith", foundCustomer.get().getLastName());
        assertEquals("jane.smith@example.com", foundCustomer.get().getEmail());
        assertEquals(25, foundCustomer.get().getAge());
    }

    @Test
    public void testFindAllCustomers() {
        // Create and save multiple customers
        Customer customer1 = new Customer();
        customer1.setFirstName("Alice");
        customer1.setLastName("Johnson");
        customer1.setEmail("alice.johnson@example.com");
        customer1.setAge(35);
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Bob");
        customer2.setLastName("Brown");
        customer2.setEmail("bob.brown@example.com");
        customer2.setAge(40);
        customerRepository.save(customer2);

        // Find all customers
        Iterable<Customer> customers = customerRepository.findAll();
        List<Customer> customerList = new ArrayList<>();
        customers.forEach(customerList::add);

        // Verify all customers were found
        assertEquals(2, customerList.size());
    }

    @Test
    public void testUpdateCustomer() {
        // Create and save a customer
        Customer customer = new Customer();
        customer.setFirstName("Michael");
        customer.setLastName("Wilson");
        customer.setEmail("michael.wilson@example.com");
        customer.setAge(45);
        Customer savedCustomer = customerRepository.save(customer);

        // Update the customer
        savedCustomer.setFirstName("Mike");
        savedCustomer.setAge(46);
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        // Verify the customer was updated
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
        assertEquals("Mike", updatedCustomer.getFirstName());
        assertEquals("Wilson", updatedCustomer.getLastName());
        assertEquals("michael.wilson@example.com", updatedCustomer.getEmail());
        assertEquals(46, updatedCustomer.getAge());
    }

    @Test
    public void testDeleteCustomer() {
        // Create and save a customer
        Customer customer = new Customer();
        customer.setFirstName("David");
        customer.setLastName("Lee");
        customer.setEmail("david.lee@example.com");
        customer.setAge(50);
        Customer savedCustomer = customerRepository.save(customer);

        // Delete the customer
        customerRepository.deleteById(savedCustomer.getId());

        // Verify the customer was deleted
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        assertFalse(foundCustomer.isPresent());
    }
}