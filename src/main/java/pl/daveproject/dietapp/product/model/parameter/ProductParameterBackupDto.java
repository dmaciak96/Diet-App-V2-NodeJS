package pl.daveproject.dietapp.product.model.parameter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductParameterBackupDto(UUID id,
                                        String name,
                                        String value) {
}
