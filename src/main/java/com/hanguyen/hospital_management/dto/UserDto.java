// src/main/java/com/hospitalmanagement/dto/UserDto.java
package com.hanguyen.hospital_management.dto;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String fullName;
    private String email;
    private String role;
}