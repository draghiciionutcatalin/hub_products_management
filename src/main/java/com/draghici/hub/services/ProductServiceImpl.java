package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    @Override
    public Page<Product> getAll(Pageable pageable) {
        return null;
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
