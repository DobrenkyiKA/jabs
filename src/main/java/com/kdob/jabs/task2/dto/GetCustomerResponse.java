package com.kdob.jabs.task2.dto;

public record GetCustomerResponse(long id, String firstName, String lastName, String email, int age) {}