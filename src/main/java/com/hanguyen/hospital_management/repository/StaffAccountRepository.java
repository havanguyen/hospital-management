// src/main/java/com/hospitalmanagement/repository/StaffAccountRepository.java
package com.hanguyen.hospital_management.repository;


import com.hanguyen.hospital_management.entity.StaffAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffAccountRepository extends JpaRepository<StaffAccount, Integer> {
    Optional<StaffAccount> findByEmail(String email);
}