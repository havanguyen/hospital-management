// src/main/java/com/hospitalmanagement/dto/LoginRequest.java
package com.hanguyen.hospital_management.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}