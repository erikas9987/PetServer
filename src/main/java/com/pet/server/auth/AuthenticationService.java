package com.pet.server.auth;

import com.pet.server.config.JwtService;
import com.pet.server.errors.UserAlreadyExistsException;
import com.pet.server.errors.UserNotFoundException;
import com.pet.server.model.Role;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import com.pet.server.requests.AuthenticationRequest;
import com.pet.server.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> getExisting = userRepository.findByEmail(request.getEmail());
        if (getExisting.isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthDate(request.getBirthDate())
                .role(Role.User)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isLoggedIn(String email) {
        return true;
    }
}
