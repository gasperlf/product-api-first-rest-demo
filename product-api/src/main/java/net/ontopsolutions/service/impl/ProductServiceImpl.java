package net.ontopsolutions.service.impl;

import lombok.RequiredArgsConstructor;
import net.ontopsolutions.exception.ProductNotFoundException;
import net.ontopsolutions.model.entity.Product;
import net.ontopsolutions.model.repository.ProductRepository;
import net.ontopsolutions.product.api.model.ProductPageResponse;
import net.ontopsolutions.product.api.model.ProductRequest;
import net.ontopsolutions.product.api.model.ProductResponse;
import net.ontopsolutions.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Random random = new Random();

    @Override
    public ProductResponse getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException(String.format("Product with id %s was not found", productId)));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setId(random.nextInt());
        product.setName(product.getName());
        product.setDescription("DESCRIPTION: " + product.getName());
        product.setPrice(product.getPrice());

        Product productSaved = productRepository.save(product);

        return ProductResponse.builder()
                .id(productSaved.getId())
                .name(productSaved.getName())
                .build();
    }

    @Override
    public ProductPageResponse getProducts(Integer page, Integer size, List<String> sort) {
        Sort orders;
        if(sort!= null && !sort.isEmpty()) {
            orders = Sort.by(sort.stream()
                    .map(value-> {
                        String[] split = value.split(":");
                        return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);})
                    .collect(Collectors.toList())
            );
        }else {
            orders = Sort.by(Sort.Direction.ASC, "name");
        }

        Pageable pageable = PageRequest.of(page, size, orders);
        Page<Product> productPage = productRepository.findAll(pageable);
        return ProductPageResponse.builder()
                .content(productPage.getContent().stream().map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .build()).collect(Collectors.toList()))
                .currentPage(productPage.getNumber())
                .empty(productPage.isEmpty())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .totalPages(productPage.getTotalPages())
                .pageSize(size)
                .currentPage(page)
                .totalElements(productPage.getTotalElements())
                .build();
    }
}
