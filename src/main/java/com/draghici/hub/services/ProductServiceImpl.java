package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.exceptions.ProductException;
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
    private final static int HTTP_NOT_FOUND = 404;
    private final static int HTTP_UNPROCESSABLE_ENTITY = 422;

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
            productNegativeIdException();
        }
        return productRepository.getProductById(id).orElseThrow(() -> productMissingException(id));
    }

    @Override
    public Product add(ProductDTO productDto) throws Exception {
        logger.info("API Request: add new product");

        if (productDto == null) {
            throw new ProductException("Cannot add a null product", HTTP_UNPROCESSABLE_ENTITY);
        }
        if ("".equals(productDto.getName()) || productDto.getPrice() < 0.0d) {
            throw new ProductException("Please provide a name and a price for the product", HTTP_UNPROCESSABLE_ENTITY);
        }

        var product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        var newProduct = productRepository.save(product);

        logger.info("A product was added with id {}", newProduct.getId());
        return newProduct;
    }

    @Override
    public Product update(Long id, ProductDTO productDto) throws Exception {
        logger.info("API Request: update product");

        if (productDto == null) {
            throw new ProductException("Cannot update a null product", HTTP_UNPROCESSABLE_ENTITY);
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
            productNegativeIdException();
        }

        var product = productRepository.getProductById(id).orElseThrow(() -> productMissingException(id));
        productRepository.delete(product);

        logger.info("Product with id {} was deleted", id);
    }

    private static Exception productMissingException(Long id) {
        return new ProductException("Product with id " + id + " not found", HTTP_NOT_FOUND);
    }

    private static Exception productNegativeIdException() {
        return new ProductException("A product with negative id cannot exist", HTTP_UNPROCESSABLE_ENTITY);
    }
}
