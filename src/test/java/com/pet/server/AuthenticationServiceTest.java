package com.pet.server.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pet.server.config.JwtService;
import com.pet.server.errors.UserAlreadyExistsException;
import com.pet.server.errors.UserNotFoundException;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import com.pet.server.requests.AuthenticationRequest;
import com.pet.server.requests.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("test@email.com", "First", "Last", "password", LocalDate.parse("2000-01-01"));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        assertDoesNotThrow(() -> authenticationService.register(request));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@email.com", "First", "Last", "password", LocalDate.parse("2000-01-01"));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.register(request));
    }

    @Test
    public void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password");
        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");
        user.setPassword("password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        assertDoesNotThrow(() -> authenticationService.authenticate(request));
    }

    @Test
    public void testAuthenticateUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("nonexistent@email.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    public void testAuthenticateIncorrectPassword() {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "wrongpassword");
        User user = new User();
        user.setId(1);
        user.setEmail("test@email.com");
        user.setPassword("password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword())
        ));
    }
}
