package pl.daveproject.webdiet.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID productId) {
        super("Product not found by id %s".formatted(productId));
    }

    public ProductNotFoundException(String productName) {
        super("Product not found by name %s".formatted(productName));
    }

}
