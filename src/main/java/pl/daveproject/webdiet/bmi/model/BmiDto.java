package pl.daveproject.webdiet.bmi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BmiDto {

    private UUID id;
    private double weight;
    private double height;
    private double value;
    private UnitSystem unit;
    private LocalDate addedDate;
    private BmiRate rate;
}
