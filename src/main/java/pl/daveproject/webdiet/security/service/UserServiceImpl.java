package pl.daveproject.webdiet.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.daveproject.webdiet.exception.UserAlreadyExistsException;
import pl.daveproject.webdiet.exception.UserNotFoundException;
import pl.daveproject.webdiet.exception.UserNotLoginException;
import pl.daveproject.webdiet.security.model.ApplicationUser;
import pl.daveproject.webdiet.security.model.Authority;
import pl.daveproject.webdiet.security.model.Role;
import pl.daveproject.webdiet.security.repository.ApplicationUserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return applicationUserRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return applicationUserRepository.existsByEmail(email);
    }

    @Override
    public boolean hasRole(ApplicationUser applicationUser, Role role) {
        return applicationUser.getAuthorities().stream().anyMatch(authority -> authority.getRole() == role);
    }

    @Override
    @Transactional
    public ApplicationUser registerNewUser(ApplicationUser applicationUser) {
        if (applicationUser.getId() == null && applicationUserRepository.existsByEmail(applicationUser.getEmail())) {
            throw new UserAlreadyExistsException(applicationUser.getEmail());
        }
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUser.setEnabled(true);
        if (applicationUser.getAuthorities() == null || applicationUser.getAuthorities().isEmpty()) {
            applicationUser.setAuthorities(List.of(Authority.builder()
                    .applicationUser(applicationUser)
                    .email(applicationUser.getEmail())
                    .role(Role.STANDARD_USER)
                    .build()));
        }
        return applicationUserRepository.save(applicationUser);
    }

    @Override
    @Transactional
    public ApplicationUser getCurrentUser() {
        var userDetails = securityService.getCurrentUser();
        if (userDetails == null) {
            throw new UserNotLoginException();
        }
        return applicationUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }
}
