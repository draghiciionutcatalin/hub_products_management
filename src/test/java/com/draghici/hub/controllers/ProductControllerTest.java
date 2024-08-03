package com.draghici.hub.controllers;

import com.draghici.hub.beans.Product;
import com.draghici.hub.repositories.ProductRepository;
import com.draghici.hub.services.ProductServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    private final static Logger logger = LogManager.getLogger(ProductControllerTest.class);

    @InjectMocks
    private ProductController productController;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MockMvc mockMvc;

    // mock 2 DB products
    Product productA;
    Product productB;

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this.getClass());

        productA = new Product();
        productA.setId(1L);
        productA.setName("Product A test");
        productA.setPrice(19.2);

        productB = new Product();
        productB.setId(2L);
        productB.setName("Product B test");
        productB.setPrice(7.09);

        productRepository.saveAll(List.of(productA, productB));

        productService = new ProductServiceImpl(productRepository);
        productController = new ProductController(productService);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @Order(1)
    void test_getListProduct() throws Exception {
        logger.info("test listProduct() in ProductController");

        Pageable pageable = PageRequest.of(0, 10);
        List<Product> list = new ArrayList<>();
        list.add(productA);
        list.add(productB);
        Page<Product> productPage = new PageImpl<>(list, pageable, list.size());

        when(productService.getAll(pageable)).thenReturn(productPage);

        Page<Product> expectedResult = productController.listProduct(pageable);

        assertNotNull(expectedResult);
        assertEquals(2, expectedResult.getTotalElements(), "Total elements should be 2");
        assertEquals(1L, expectedResult.getContent().get(0).getId(), "Product ID should match");
        assertEquals("Product A test", expectedResult.getContent().get(0).getName(), "Product name should match");
        assertEquals(7.09, expectedResult.getContent().get(1).getPrice(), "Product price should match");
    }


}