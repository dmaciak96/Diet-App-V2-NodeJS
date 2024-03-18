package pl.daveproject.webdiet.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {
    void logout();

    UserDetails getCurrentUser();
}
