// src/main/java/com/hospitalmanagement/dto/ChangePasswordRequest.java
package com.hanguyen.hospital_management.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}