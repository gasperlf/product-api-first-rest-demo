package net.ontopsolutions.service;

import net.ontopsolutions.product.api.model.ProductPageResponse;
import net.ontopsolutions.product.api.model.ProductRequest;
import net.ontopsolutions.product.api.model.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse getProductById(Integer productId);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductPageResponse getProducts(Integer page, Integer size, List<String> sort);
}
