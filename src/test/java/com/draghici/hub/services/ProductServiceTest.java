package com.draghici.hub.services;


import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.exceptions.ProductException;
import com.draghici.hub.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    void test_getAll() {
        logger.info("test getAll()");

        var pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(productList));

        Page<Product> result = productService.getAll(pageable);

        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals(1L, result.getContent().get(0).getId(), "Product ID should match");
        assertEquals("Product A test", result.getContent().get(0).getName(), "Product name should match");
        assertEquals(7.09, result.getContent().get(1).getPrice(), "Product price should match");

    }

    @Test
    @Order(2)
    void test_getById() {
        logger.info("test getById() for existing product");

        Long targetID = 1L;
        when(productRepository.getProductById(targetID)).thenReturn(Optional.ofNullable(productA));

        var result = productService.getById(targetID);

        assertNotNull(result);
        assertEquals(targetID, result.getId(), "Product ID should match");
        assertEquals("Product A test", result.getName(), "Product name should match");
    }

    @Test
    @Order(3)
    void test_getByWrongId() {
        logger.info("test getById() for missing product");

        Long targetID = 153234123L;
        when(productRepository.getProductById(targetID)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> {
            productService.getById(targetID);
        });

        assertEquals("Product with id " + targetID + " not found", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(4)
    void test_getByNegativeId() {
        logger.info("test getById() for negative product id");

        Long targetID = -1L;

        ProductException exception = assertThrows(ProductException.class, () -> productService.getById(targetID));

        assertEquals("A product with negative id cannot exist", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(5)
    void test_addNewProduct() {
        logger.info("test add() for a new product");

        ProductDTO productDTO = ProductDTO.builder().name("Product C test").price(87.2).build();

        Product product = new Product();
        product.setId(4L);
        product.setName("Product C test");
        product.setPrice(87.2);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product expectedResult = productService.add(productDTO);

        assertNotNull(expectedResult);
        assertEquals(product.getId(), expectedResult.getId(), "Product ID should match");
        assertEquals(product.getName(), expectedResult.getName(), "Product name should match");
        assertEquals(product.getPrice(), expectedResult.getPrice(), "Product price should match");
    }

    @Test
    @Order(6)
    void test_addNullProduct() {
        logger.info("test add() for a null product");

        ProductException exception = assertThrows(ProductException.class, () -> productService.add(null));

        assertEquals("Cannot add a null product", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(7)
    void test_addFaultyProduct() {
        logger.info("test add() for a faulty product");

        ProductDTO productDTO = ProductDTO.builder().name("").price(187.2).build();

        ProductException exception = assertThrows(ProductException.class, () -> productService.add(productDTO));

        assertEquals("Please provide a name and a positive price for the product", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(8)
    void test_addFaultyProduct2() {
        logger.info("test add() for a faulty product 2");

        ProductDTO productDTO = ProductDTO.builder().name("AAA").price(-1.1).build();

        ProductException exception = assertThrows(ProductException.class, () -> productService.add(productDTO));

        assertEquals("Please provide a name and a positive price for the product", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(9)
    void test_updateProduct() {
        logger.info("test update() for a product");

        ProductDTO productDTO = ProductDTO.builder().name(productA.getName()).price(87.2).build();

        Product product = productA;
        product.setPrice(87.2);
        Long targetID = productA.getId();

        when(productRepository.getProductById(targetID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product expectedResult = productService.update(targetID, productDTO);

        assertNotNull(expectedResult);
        assertEquals(product.getId(), expectedResult.getId(), "Product ID should match");
        assertEquals(product.getName(), expectedResult.getName(), "Product name should match");
        assertEquals(product.getPrice(), expectedResult.getPrice(), "Product price should match");
    }

    @Test
    @Order(10)
    void test_updateNullProduct() {
        logger.info("test update() for a null product");

        ProductException exception = assertThrows(ProductException.class, () -> productService.update(productA.getId(), null));

        assertEquals("Cannot update a null product", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(11)
    void test_updateMissingProduct() {
        logger.info("test update() for a missing product");

        ProductDTO productDTO = ProductDTO.builder().name(productA.getName()).price(productA.getPrice()).build();
        Long targetID = 455689L;

        when(productRepository.getProductById(targetID)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.update(targetID, productDTO));

        assertEquals("Product with id " + targetID + " not found", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(12)
    void test_updateFaultyProduct() {
        logger.info("test update() for a faulty product");

        ProductDTO productDTO = ProductDTO.builder().name("").price(187.2).build();

        ProductException exception = assertThrows(ProductException.class, () -> productService.update(1L, productDTO));

        assertEquals("Please provide a name and a positive price for the product", exception.getMessage(), "Exception message should match");
    }

    @Test
    @Order(13)
    void test_updateFaultyProduct2() {
        logger.info("test update() for a faulty product 2");

        ProductDTO productDTO = ProductDTO.builder().name("BBB").price(-41.1).build();

        ProductException exception = assertThrows(ProductException.class, () -> productService.update(2L, productDTO));

        assertEquals("Please provide a name and a positive price for the product", exception.getMessage(), "Exception message should match");
    }
}
