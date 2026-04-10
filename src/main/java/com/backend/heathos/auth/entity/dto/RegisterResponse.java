package com.backend.heathos.auth.entity.dto;

import com.backend.heathos.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private UUID userId;
    private String fullName;
    private String email;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
}
