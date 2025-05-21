package com.kdob.jabs.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdob.jabs.task2.dao.Customer;
import com.kdob.jabs.task2.dto.CreateCustomerRequest;
import com.kdob.jabs.task2.dto.UpdateCustomerRequest;
import com.kdob.jabs.task2.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        customerRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCustomerCrudOperations() throws Exception {
        // 1. Create a customer
        CreateCustomerRequest createRequest = new CreateCustomerRequest(
                "John", "Doe", "john.doe@example.com", 30);

        String createResponse = mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andReturn().getResponse().getContentAsString();

        // Extract the ID from the response
        Long customerId = objectMapper.readTree(createResponse).get("id").asLong();

        // 2. Get the customer
        mockMvc.perform(get("/customers/" + customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30));

        // 3. Update the customer
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(
                "Jane", "Smith", "jane.smith@example.com", 25);

        mockMvc.perform(put("/customers/" + customerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        // 4. Verify the update in the database
        Customer updatedCustomer = customerRepository.findById(customerId).orElse(null);
        assertNotNull(updatedCustomer);
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Smith", updatedCustomer.getLastName());
        assertEquals("jane.smith@example.com", updatedCustomer.getEmail());
        assertEquals(25, updatedCustomer.getAge());

        // 5. Delete the customer
        mockMvc.perform(delete("/customers/" + customerId)
                .with(csrf()))
                .andExpect(status().isOk());

        // 6. Verify the customer was deleted
        assertFalse(customerRepository.existsById(customerId));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserRolePermissions() throws Exception {
        // Create a test customer in the database
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test.user@example.com");
        customer.setAge(40);
        Customer savedCustomer = customerRepository.save(customer);

        // USER role should be able to get a customer
        mockMvc.perform(get("/customers/" + savedCustomer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"));

        // USER role should not be able to create a customer
        CreateCustomerRequest createRequest = new CreateCustomerRequest(
                "New", "User", "new.user@example.com", 35);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // USER role should not be able to update a customer
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(
                "Updated", "User", "updated.user@example.com", 36);

        mockMvc.perform(put("/customers/" + savedCustomer.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // USER role should not be able to delete a customer
        mockMvc.perform(delete("/customers/" + savedCustomer.getId())
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Verify the customer was not deleted
        assertTrue(customerRepository.existsById(savedCustomer.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetNonExistentCustomer() throws Exception {
        // Try to get a customer with a non-existent ID
        mockMvc.perform(get("/customers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer with ID: [999] not found."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateMultipleCustomers() throws Exception {
        // Create first customer
        CreateCustomerRequest request1 = new CreateCustomerRequest(
                "Alice", "Johnson", "alice.johnson@example.com", 28);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        // Create second customer
        CreateCustomerRequest request2 = new CreateCustomerRequest(
                "Bob", "Brown", "bob.brown@example.com", 35);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        // Verify both customers exist in the database
        assertEquals(2, customerRepository.count());
    }
}
