package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAll(Pageable pageable) {
        logger.info("Request: getAll");
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getOne(Long id) {
        return null;
    }

    @Override
    public Product add(ProductDTO productDto) {
        return null;
    }

    @Override
    public Product update(Long id, ProductDTO productDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
