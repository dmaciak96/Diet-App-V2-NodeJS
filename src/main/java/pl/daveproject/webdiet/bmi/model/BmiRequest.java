package pl.daveproject.webdiet.bmi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BmiRequest {

    @NotNull(message = "Weight cannot be null")
    @Min(value = 0, message = "Weight cannot be less than 0")
    private double weight;

    @NotNull(message = "Height cannot be null")
    @Min(value = 0, message = "Height cannot be less than 0")
    private double height;

    @NotNull(message = "Unit cannot be null")
    private UnitSystem unit;
}
