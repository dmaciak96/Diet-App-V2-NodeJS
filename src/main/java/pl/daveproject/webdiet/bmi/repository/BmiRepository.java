package pl.daveproject.webdiet.bmi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.webdiet.bmi.model.Bmi;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BmiRepository extends JpaRepository<Bmi, UUID> {

    List<Bmi> findAllByApplicationUserId(UUID applicationUserId);

    Optional<Bmi> findFirstByApplicationUserIdAndId(UUID userId, UUID id);

    List<Bmi> findAllByApplicationUserIdAndAddedDateIsBetween(UUID userId, LocalDate from, LocalDate to);
}
