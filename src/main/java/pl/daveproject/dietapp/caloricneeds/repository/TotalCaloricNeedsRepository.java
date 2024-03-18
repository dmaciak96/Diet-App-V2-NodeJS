package pl.daveproject.dietapp.caloricneeds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.daveproject.dietapp.caloricneeds.model.TotalCaloricNeeds;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TotalCaloricNeedsRepository extends JpaRepository<TotalCaloricNeeds, UUID> {

    List<TotalCaloricNeeds> findAllByApplicationUserId(UUID applicationUserId);

    Optional<TotalCaloricNeeds> findFirstByApplicationUserIdAndId(UUID userId, UUID id);

    List<TotalCaloricNeeds> findAllByApplicationUserIdAndAddedDateIsBetween(UUID userId, LocalDate from,
                                                                            LocalDate to);
}
