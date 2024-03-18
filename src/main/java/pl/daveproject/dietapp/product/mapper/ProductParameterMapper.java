package pl.daveproject.dietapp.product.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.product.model.parameter.ProductParameter;
import pl.daveproject.dietapp.product.model.parameter.ProductParameterDto;
import pl.daveproject.dietapp.product.model.parameter.ProductParameterRequest;

import java.util.List;

@Mapper
public interface ProductParameterMapper {

    ProductParameterDto entityToDto(ProductParameter productParameter);

    List<ProductParameterDto> entitiesToDtoList(List<ProductParameter> productParameter);

    ProductParameter dtoToEntity(ProductParameterDto productParameterDto);

    List<ProductParameter> dtoListToEntities(List<ProductParameterDto> dtoList);

    ProductParameterDto requestToDto(ProductParameterRequest request);

    List<ProductParameterDto> requestsToDtoList(List<ProductParameterRequest> requests);
}
