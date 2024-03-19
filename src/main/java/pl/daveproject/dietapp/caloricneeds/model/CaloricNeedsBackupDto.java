package pl.daveproject.dietapp.caloricneeds.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import pl.daveproject.dietapp.bmi.model.UnitSystem;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CaloricNeedsBackupDto(UUID id,
                                    double weight,
                                    double height,
                                    double value,
                                    LocalDate addedDate,
                                    UnitSystem unit,
                                    Gender gender,
                                    ActivityLevel activityLevel,
                                    int age) {
}
