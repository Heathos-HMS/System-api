package com.backend.heathos.auth.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;   // the JWT string
    private String role;    // "DOCTOR", "ADMIN"  — so the frontend knows which screen to show
    private UUID userId;    // the user's ID
}
