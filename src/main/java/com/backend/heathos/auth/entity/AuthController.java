package com.backend.heathos.auth.entity;

import com.backend.heathos.auth.entity.dto.LoginRequest;
import com.backend.heathos.auth.entity.dto.LoginResponse;
import com.backend.heathos.auth.entity.dto.RegisterRequest;
import com.backend.heathos.auth.entity.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication",
        description = "Public endpoints for staff login and registration. No token required."
)
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    // Body: { "fullName": "Dr. Kwame Asante", "email": "kwame@hospital.com",
    //         "password": "secure123", "role": "DOCTOR", "phone": "0244000001" }
    @PostMapping("/register")
    @Operation(
            summary = "Register a new staff account",
            description = "Creates a new staff user. Role must be one of: ADMIN, RECEPTIONIST, " +
                    "DOCTOR, NURSE, LAB_TECHNICIAN, BILLING_OFFICER, PHARMACIST. " +
                    "A matching profile row is created automatically based on the role."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed — missing or invalid fields"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(response));
    }

    // POST /api/auth/login
    // Body: { "email": "kwame@hospital.com", "password": "secure123" }
    @PostMapping("/login")
    @Operation(
            summary = "Login and get a JWT token",
            description = "Authenticate with email and password. Returns a JWT token valid for 24 hours. " +
                    "Copy the token and click the 'Authorize' button at the top of this page to use it."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful — JWT token returned"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Account is deactivated")
    })
    public ResponseEntity<com.backend.heathos.common.ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(com.backend.heathos.common.ApiResponse.success(response));
    }
}
