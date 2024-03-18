package pl.daveproject.dietapp.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.dietapp.security.model.ApplicationUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, UUID> {

    boolean existsByEmail(String email);

    Optional<ApplicationUser> findByEmail(String email);
}
