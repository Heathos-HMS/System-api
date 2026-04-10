package com.backend.heathos.auth.entity;

import com.backend.heathos.auth.entity.dto.LoginRequest;
import com.backend.heathos.auth.entity.dto.LoginResponse;
import com.backend.heathos.auth.entity.dto.RegisterRequest;
import com.backend.heathos.auth.entity.dto.RegisterResponse;
import com.backend.heathos.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    // Body: { "fullName": "Dr. Kwame Asante", "email": "kwame@hospital.com",
    //         "password": "secure123", "role": "DOCTOR", "phone": "0244000001" }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // POST /api/auth/login
    // Body: { "email": "kwame@hospital.com", "password": "secure123" }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
