package pl.daveproject.dietapp.product.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import pl.daveproject.dietapp.product.model.parameter.ProductParameterBackupDto;

import java.util.List;
import java.util.UUID;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductBackupDto(UUID id,
                               String name,
                               Double kcal,
                               ProductType type,
                               List<ProductParameterBackupDto> parameters) {
}
