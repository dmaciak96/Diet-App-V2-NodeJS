package pl.daveproject.dietapp.bmi.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BmiBackupDto(UUID id,
                           double weight,
                           double height,
                           double value,
                           LocalDate addedDate,
                           UnitSystem unit,
                           BmiRate rate) {
}
