// src/main/java/com/hospitalmanagement/repository/RefreshTokenRepository.java
package com.hanguyen.hospital_management.repository;


import com.hanguyen.hospital_management.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByStaffAccountId(Integer userId);
}