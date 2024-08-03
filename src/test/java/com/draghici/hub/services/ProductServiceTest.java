package com.draghici.hub.services;


import com.draghici.hub.beans.Product;
import com.draghici.hub.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    private final static Logger logger = LogManager.getLogger(ProductServiceTest.class);

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    // mock 2 DB products
    Product productA;
    Product productB;
    List<Product> productList;

    @BeforeEach
    public void setup() {
        logger.info("@BeforeEach: initial setup");

        productList = new ArrayList<>();

        productA = new Product();
        productA.setId(1L);
        productA.setName("Product A test");
        productA.setPrice(19.2);
        productList.add(productA);

        productB = new Product();
        productB.setId(2L);
        productB.setName("Product B test");
        productB.setPrice(7.09);
        productList.add(productB);

        productRepository.saveAll(List.of(productA, productB));
    }

    @Test
    @Order(1)
    void test_getAll(){
        logger.info("test getAll()");

        var pageable = PageRequest.of(0,10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(productList));

        Page<Product> result = productService.getAll(pageable);

        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals(1L, result.getContent().get(0).getId(), "Product ID should match");
        assertEquals("Product A test", result.getContent().get(0).getName(), "Product name should match");
        assertEquals(7.09, result.getContent().get(1).getPrice(), "Product price should match");

    }
}
