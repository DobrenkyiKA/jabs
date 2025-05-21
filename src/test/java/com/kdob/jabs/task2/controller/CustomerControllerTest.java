package com.kdob.jabs.task2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdob.jabs.task2.dto.*;
import com.kdob.jabs.task2.facade.CustomerFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerFacade customerFacade;

    private CreateCustomerRequest createCustomerRequest;
    private CreateCustomerResponse createCustomerResponse;
    private GetCustomerResponse getCustomerResponse;
    private UpdateCustomerRequest updateCustomerRequest;
    private UpdateCustomerResponse updateCustomerResponse;

    @BeforeEach
    void setUp() {
        createCustomerRequest = new CreateCustomerRequest("John", "Doe", "john.doe@example.com", 30);
        createCustomerResponse = new CreateCustomerResponse(1L, "John", "Doe", "john.doe@example.com", 30);
        getCustomerResponse = new GetCustomerResponse(1L, "John", "Doe", "john.doe@example.com", 30);
        updateCustomerRequest = new UpdateCustomerRequest("Jane", "Smith", "jane.smith@example.com", 25);
        updateCustomerResponse = new UpdateCustomerResponse(1L, "Jane", "Smith", "jane.smith@example.com", 25);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCustomer() throws Exception {
        when(customerFacade.createCustomer(any(CreateCustomerRequest.class))).thenReturn(createCustomerResponse);

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30));

        verify(customerFacade, times(1)).createCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateCustomer_Forbidden() throws Exception {
        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)))
                .andExpect(status().is4xxClientError());

        verify(customerFacade, never()).createCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void testGetCustomer() throws Exception {
        when(customerFacade.getCustomer(1L)).thenReturn(getCustomerResponse);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(30));

        verify(customerFacade, times(1)).getCustomer(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCustomer() throws Exception {
        when(customerFacade.updateCustomer(any(UpdateCustomerRequest.class), eq(1L))).thenReturn(updateCustomerResponse);

        mockMvc.perform(put("/customers/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(customerFacade, times(1)).updateCustomer(any(UpdateCustomerRequest.class), eq(1L));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateCustomer_Forbidden() throws Exception {
        mockMvc.perform(put("/customers/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCustomerRequest)))
                .andExpect(status().is4xxClientError());

        verify(customerFacade, never()).updateCustomer(any(UpdateCustomerRequest.class), anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerFacade).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1")
                .with(csrf()))
                .andExpect(status().isOk());

        verify(customerFacade, times(1)).deleteCustomer(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteCustomer_Forbidden() throws Exception {
        mockMvc.perform(delete("/customers/1")
                .with(csrf()))
                .andExpect(status().is4xxClientError());

        verify(customerFacade, never()).deleteCustomer(anyLong());
    }
}
