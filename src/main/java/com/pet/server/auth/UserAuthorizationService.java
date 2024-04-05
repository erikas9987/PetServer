package com.pet.server.auth;

import com.pet.server.errors.UserNotFoundException;
import com.pet.server.model.Role;
import com.pet.server.model.User;
import com.pet.server.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorizationService {

    private final UserRepository userRepository;

    private List<? extends GrantedAuthority> getAuthorities() {
        return (List<? extends GrantedAuthority>) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities();
    }

    private String getId() {
        return getAuthorities().get(0).getAuthority();
    }

    private Role getRole() {
        return switch (getAuthorities().get(1).getAuthority()) {
            case "User" -> Role.User;
            case "Vet" -> Role.Vet;
            case "Admin" -> Role.Admin;
            default -> throw new IllegalStateException("Unexpected value: " + getAuthorities().get(1).getAuthority());
        };
    }

    public boolean isAuthorizedUser(int id) {
        String authId = getId();
        User authUser = userRepository.findById(Integer.parseInt(authId))
                .orElseThrow(() -> new UserNotFoundException(authId));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return authUser.getEmail().equals(user.getEmail()) || getRole() == Role.Admin;
    }

    public boolean isVet() {
        return getRole() == Role.Vet || getRole() == Role.Admin;
    }

}
