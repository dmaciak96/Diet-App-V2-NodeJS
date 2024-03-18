package pl.daveproject.webdiet.caloricneeds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.daveproject.webdiet.bmi.model.UnitSystem;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalCaloricNeedsDto {

    private UUID id;
    private double weight;
    private double height;
    private double value;
    private LocalDate addedDate;
    private UnitSystem unit;
    private Gender gender;
    private ActivityLevel activityLevel;
    private int age;
}
