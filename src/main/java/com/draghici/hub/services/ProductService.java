package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<Product> getAll(Pageable pageable);

    Product getOne(Long id) throws Exception;

    Product add(ProductDTO productDto) throws Exception;

    Product update(Long id, ProductDTO productDto);

    void delete(Long id);
}
