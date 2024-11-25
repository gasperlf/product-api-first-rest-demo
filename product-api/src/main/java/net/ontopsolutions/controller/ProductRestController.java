package net.ontopsolutions.controller;

import lombok.RequiredArgsConstructor;
import net.ontopsolutions.product.api.model.ProductPageResponse;
import net.ontopsolutions.product.api.model.ProductRequest;
import net.ontopsolutions.product.api.model.ProductResponse;
import net.ontopsolutions.product.api.rest.ProductsApi;
import net.ontopsolutions.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductRestController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequest));
    }

    @Override
    public ResponseEntity<ProductResponse> getProductById(Integer productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Override
    public ResponseEntity<ProductPageResponse> getProducts(Integer page, Integer size, List<String> sort) {
        return ResponseEntity.ok(productService.getProducts(page, size, sort));
    }
}
