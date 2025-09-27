// src/main/java/com/hospitalmanagement/entity/RefreshToken.java
package com.hanguyen.hospital_management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "staff_account_id", nullable = false)
    private StaffAccount staffAccount;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    private boolean revoked = false;

    @Column(name = "created_at")
    private Date createdAt = new Date();
}