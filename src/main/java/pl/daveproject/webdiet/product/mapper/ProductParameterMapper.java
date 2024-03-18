package pl.daveproject.webdiet.product.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.webdiet.product.model.parameter.ProductParameter;
import pl.daveproject.webdiet.product.model.parameter.ProductParameterDto;
import pl.daveproject.webdiet.product.model.parameter.ProductParameterRequest;

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
