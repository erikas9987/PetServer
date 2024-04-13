package com.pet.server.auth;

import com.pet.server.model.Role;
import com.pet.server.model.User;
import com.pet.server.model.Pet;
import com.pet.server.repos.PetRepository;
import com.pet.server.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthorizationService {

    private final UserRepository userRepository;

    private final PetRepository petRepository;

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
        Optional<User> authUser = userRepository.findById(Integer.parseInt(authId));
        if (authUser.isEmpty()) {
            return false;
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return false;
        }
        if (authUser.get().getEmail().equals(user.get().getEmail()) || getRole() == Role.Admin) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource.");
        }
    }

    public boolean isAuthorizedPet(int id) {
        String authId = getId();
        Optional<User> user = userRepository.findById(Integer.parseInt(authId));
        if (user.isEmpty()) {
            return false;
        }
        User author = user.get();
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isEmpty()) {
            return false;
        }
        if (pet.get().getUser().getId() == author.getId()) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource.");
        }
    }

    public boolean isVet() {
        if (getRole() == Role.Vet || getRole() == Role.Admin) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource.");
        }
    }

}
