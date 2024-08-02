package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAll(Pageable pageable) {
        logger.info("API Request: get all products");
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getOne(Long id) throws Exception {
        logger.info("API Request: get one product by id");

        if (id < 0) {
            throw new Exception("A product with negative id cannot exist");
        }
        return productRepository.getProductById(id).orElseThrow(() -> productMissingException(id));
    }

    @Override
    public Product add(ProductDTO productDto) throws Exception {
        logger.info("API Request: add new product");

        if (productDto == null) {
            throw new Exception("Cannot add a null product");
        }

        if (productDto.getName() != null && productDto.getPrice() < 0) {
            var product = new Product();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            var newProduct = productRepository.save(product);
            logger.info("A product was added with id {}", newProduct.getId());
            return newProduct;
        } else {
            throw new Exception("Please provide a name and a price to the product");
        }
    }

    @Override
    public Product update(Long id, ProductDTO productDto) throws Exception {
        logger.info("API Request: update product");

        if (productDto == null) {
            throw new Exception("Cannot update a null product");
        }

        var product = productRepository.getProductById(id).orElseThrow(() -> productMissingException(id));

        Optional.ofNullable(productDto.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDto.getPrice()).ifPresent(product::setPrice);
        var updatedProduct = productRepository.save(product);

        logger.info("Product with id {} updated", id);
        return updatedProduct;

    }

    @Override
    public void delete(Long id) throws Exception {
        logger.info("API Request: delete product");

        if (id < 0) {
            throw new Exception("A product with negative id cannot exist");
        }

        var product = productRepository.getProductById(id).orElseThrow(() -> productMissingException());
        productRepository.delete(product);

        logger.info("Product with id {} was deleted", id);
    }

    private static Exception productMissingException(Long id) {
        return new Exception("Product with id " + id + " not found");
    }
}
