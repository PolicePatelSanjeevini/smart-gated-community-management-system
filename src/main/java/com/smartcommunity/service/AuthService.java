package com.smartcommunity.service;

import com.smartcommunity.dto.AuthDTOs.*;
import com.smartcommunity.entity.Role;
import com.smartcommunity.entity.User;
import com.smartcommunity.enums.RoleType;
import com.smartcommunity.exception.BadRequestException;
import com.smartcommunity.exception.DuplicateResourceException;
import com.smartcommunity.exception.ResourceNotFoundException;
import com.smartcommunity.repository.RoleRepository;
import com.smartcommunity.repository.UserRepository;
import com.smartcommunity.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));

        UserDTO userDTO = mapToUserDTO(user);

        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .user(userDTO)
                .build();
    }

    @Transactional
    public UserDTO registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("User", "email", registerRequest.getEmail());
        }

        if (registerRequest.getPhoneNumber() != null && userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "phoneNumber", registerRequest.getPhoneNumber());
        }

        RoleType requestedRole = registerRequest.getRole() != null ? registerRequest.getRole() : RoleType.ROLE_RESIDENT;
        Role userRole = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new BadRequestException("Role not found: " + requestedRole));

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .active(true)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        User savedUser = userRepository.save(user);
        return mapToUserDTO(savedUser);
    }

    public UserDTO mapToUserDTO(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .active(user.isActive())
                .roles(roles)
                .build();
    }
}
