package com.draghici.hub.services;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

    Product add(ProductDTO productDto);

    Product update(Long id, ProductDTO productDto);

    void delete(Long id);
}
