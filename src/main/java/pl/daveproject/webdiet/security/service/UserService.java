package pl.daveproject.webdiet.security.service;

import pl.daveproject.webdiet.security.model.ApplicationUser;
import pl.daveproject.webdiet.security.model.Role;

import java.util.Optional;

public interface UserService {
    Optional<ApplicationUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean hasRole(ApplicationUser applicationUser, Role role);

    ApplicationUser registerNewUser(ApplicationUser applicationUser);

    ApplicationUser getCurrentUser();
}
