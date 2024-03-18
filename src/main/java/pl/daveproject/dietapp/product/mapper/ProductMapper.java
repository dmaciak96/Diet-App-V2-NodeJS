package pl.daveproject.dietapp.product.mapper;

import org.mapstruct.Mapper;
import pl.daveproject.dietapp.product.model.Product;
import pl.daveproject.dietapp.product.model.ProductDto;
import pl.daveproject.dietapp.product.model.ProductRequest;

import java.util.List;

@Mapper(uses = ProductParameterMapper.class)
public interface ProductMapper {

    ProductDto entityToDto(Product product);

    List<ProductDto> entitiesToDtoList(List<Product> products);

    Product dtoToEntity(ProductDto dto);

    ProductDto requestToDto(ProductRequest request);

    ProductRequest dtoToRequest(ProductDto dto);
}
