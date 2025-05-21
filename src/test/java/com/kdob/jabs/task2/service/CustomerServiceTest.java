package com.kdob.jabs.task2.service;

import com.kdob.jabs.task2.dao.Customer;
import com.kdob.jabs.task2.exception.CustomerNotFoundException;
import com.kdob.jabs.task2.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setAge(30);
    }

    @Test
    void testCreateCustomer() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        Customer result = customerService.createCustomer(testCustomer);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals(30, result.getAge());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomer_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        Customer result = customerService.getCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomer_NotFound() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.getCustomer(99L)
        );
        
        assertEquals("Customer with ID: [99] not found.", exception.getMessage());
        verify(customerRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateCustomer_Success() {
        // Arrange
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setLastName("Smith");
        updatedCustomer.setEmail("jane.smith@example.com");
        updatedCustomer.setAge(25);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        Customer result = customerService.updateCustomer(updatedCustomer, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getEmail());
        assertEquals(25, result.getAge());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        // Arrange
        Customer updatedCustomer = new Customer();
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.updateCustomer(updatedCustomer, 99L)
        );
        
        assertEquals("Customer with ID: [99] not found.", exception.getMessage());
        verify(customerRepository, times(1)).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        // Arrange
        doNothing().when(customerRepository).deleteById(1L);

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).deleteById(1L);
    }
}