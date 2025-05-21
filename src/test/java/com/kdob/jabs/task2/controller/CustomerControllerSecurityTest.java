package com.kdob.jabs.task2.controller;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    private Long testCustomerId;

    @BeforeEach
    public void setUp() {
        // Clear the database
        customerRepository.deleteAll();

        // Create a test customer
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test.user@example.com");
        customer.setAge(30);
        Customer savedCustomer = customerRepository.save(customer);
        testCustomerId = savedCustomer.getId();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCanGetCustomer() throws Exception {
        mockMvc.perform(get("/customers/" + testCustomerId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanGetCustomer() throws Exception {
        mockMvc.perform(get("/customers/" + testCustomerId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCannotCreateCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "john.doe@example.com", 30);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()); // Either 400 or 403 is acceptable
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanCreateCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "john.doe@example.com", 30);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCannotUpdateCustomer() throws Exception {
        UpdateCustomerRequest request = new UpdateCustomerRequest("John", "Doe", "john.doe@example.com", 30);

        mockMvc.perform(put("/customers/" + testCustomerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError()); // Either 400 or 403 is acceptable
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanUpdateCustomer() throws Exception {
        UpdateCustomerRequest request = new UpdateCustomerRequest("John", "Doe", "john.doe@example.com", 30);

        mockMvc.perform(put("/customers/" + testCustomerId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserCannotDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/" + testCustomerId)
                .with(csrf()))
                .andExpect(status().is4xxClientError()); // Either 400 or 403 is acceptable
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/" + testCustomerId)
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
