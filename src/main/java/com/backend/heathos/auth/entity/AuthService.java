package com.backend.heathos.auth.entity;

import com.backend.heathos.auth.entity.dto.LoginRequest;
import com.backend.heathos.auth.entity.dto.LoginResponse;
import com.backend.heathos.auth.entity.dto.RegisterRequest;
import com.backend.heathos.auth.entity.User;
import com.backend.heathos.common.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;  // This is the BCryptPasswordEncoder from SecurityConfig

    // ── REGISTER a new staff member ───────────────────────────
    public User register(RegisterRequest request) {

        // Check if this email is already taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists");
        }

        // Build the new User object
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword())); // NEVER store plain text
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setActive(true);  // new accounts are active by default

        // Save to database and return the saved user (now has an auto-generated id)
        return userRepository.save(user);
    }

    // ── LOGIN an existing staff member ────────────────────────
    public LoginResponse login(LoginRequest request) {

        // Step 1: Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        // Step 2: Check if account is active
        if (!user.isActive()) {
            throw new RuntimeException("This account has been deactivated");
        }

        // Step 3: Check password
        // BCrypt compares the plain text password with the stored hash
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            throw new RuntimeException("Incorrect password");
        }

        // Step 4: Generate JWT token
        String token = jwtUtil.generateToken(user);

        // Step 5: Return the token + role + userId
        return new LoginResponse(token, user.getRole().name(), user.getId());
    }
}
