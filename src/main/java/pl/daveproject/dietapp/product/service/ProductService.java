package pl.daveproject.dietapp.product.service;


import pl.daveproject.dietapp.product.model.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> findAll();

    Optional<ProductDto> findById(UUID id);

    Optional<ProductDto> findByName(String name);

    ProductDto save(ProductDto productDto);

    ProductDto update(UUID id, ProductDto productDto);

    void delete(ProductDto productDto);
}
