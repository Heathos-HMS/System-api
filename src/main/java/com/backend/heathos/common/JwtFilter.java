package com.backend.heathos.common;

import com.backend.heathos.auth.entity.UserRepository;
import com.backend.heathos.auth.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 1: Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: If no header or wrong format, skip — security rules handle it later
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract the token — remove "Bearer " (7 characters) from the front
        String token = authHeader.substring(7);

        // Step 4: Validate the token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5: Extract the userId from the token
        UUID userId = jwtUtil.getUserIdFromToken(token);

        // Step 6: Load the user from database to confirm they still exist and are active
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.isActive()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 7: Create an authentication object with the user's role
        // "ROLE_" prefix is required by Spring Security for @PreAuthorize to work
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, List.of(authority));

        // Step 8: Store in SecurityContext — now Spring knows who is making this request
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Step 9: Pass request to the next filter (and eventually the Controller)
        filterChain.doFilter(request, response);
    }

}
