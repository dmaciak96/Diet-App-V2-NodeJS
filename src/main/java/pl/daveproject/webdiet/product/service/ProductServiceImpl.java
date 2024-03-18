package pl.daveproject.webdiet.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daveproject.webdiet.product.mapper.ProductMapper;
import pl.daveproject.webdiet.product.model.ProductDto;
import pl.daveproject.webdiet.product.repository.ProductRepository;
import pl.daveproject.webdiet.security.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;

    @Override
    public List<ProductDto> findAll() {
        var currentUser = userService.getCurrentUser();
        var products = productRepository.findAllByApplicationUserId(currentUser.getId());
        log.debug("Mapping {} entities to dto", products.size());
        return productMapper.entitiesToDtoList(products);
    }

    @Override
    public Optional<ProductDto> findById(UUID id) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find product by id {} for user {}", id, currentUser.getEmail());
        var product = productRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        return product.map(productMapper::entityToDto);
    }

    @Override
    public Optional<ProductDto> findByName(String name) {
        var currentUser = userService.getCurrentUser();
        log.debug("Find product by id {} for user {}", name, currentUser.getEmail());
        var product = productRepository.findFirstByApplicationUserIdAndNameIgnoreCase(currentUser.getId(), name);
        return product.map(productMapper::entityToDto);
    }

    @Override
    public ProductDto update(UUID id, ProductDto productDto) {
        var currentUser = userService.getCurrentUser();
        var currentEntity = productRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), id);
        var entityToUpdate = currentEntity.map(e -> {
            var sourceEntity = productMapper.dtoToEntity(productDto);
            e.setName(sourceEntity.getName());
            e.setType(sourceEntity.getType());
            e.setKcal(sourceEntity.getKcal());
            e.setParameters(sourceEntity.getParameters());
            e.getParameters().forEach(productParameter -> productParameter.setProduct(e));
            return e;
        }).orElseThrow();
        var updatedEntity = productRepository.save(entityToUpdate);
        return productMapper.entityToDto(updatedEntity);
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        var currentUser = userService.getCurrentUser();
        var product = productMapper.dtoToEntity(productDto);
        product.setApplicationUser(currentUser);
        product.getParameters().forEach(productParameter -> productParameter.setProduct(product));
        var savedEntity = productRepository.save(product);
        log.debug("Saved new Product {} - {}", savedEntity.getName(), savedEntity.getId());
        return productMapper.entityToDto(savedEntity);
    }

    @Override
    public void delete(ProductDto productDto) {
        var currentUser = userService.getCurrentUser();
        if (productRepository.findFirstByApplicationUserIdAndId(currentUser.getId(), productDto.getId()).isPresent()) {
            log.debug("Deleting product {} - {}", productDto.getName(), productDto.getId());
            productRepository.deleteById(productDto.getId());
        }
    }
}
