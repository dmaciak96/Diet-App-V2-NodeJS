package pl.daveproject.dietapp.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {
    void logout();

    UserDetails getCurrentUser();
}
