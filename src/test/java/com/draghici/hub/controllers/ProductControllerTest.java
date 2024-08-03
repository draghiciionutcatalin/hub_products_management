package com.draghici.hub.controllers;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.repositories.ProductRepository;
import com.draghici.hub.services.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Test
    @Order(2)
    void test_getOneProduct() throws Exception {
        logger.info("test getOne() in ProductController");

        when(productRepository.getProductById(1L)).thenReturn(Optional.ofNullable(productA));

        MvcResult result = mockMvc.perform(get("/api/product/1")
                        .content(String.valueOf(productA)))
                .andExpect(status().isOk())
                .andReturn();

        String responseProductAsJson = result.getResponse().getContentAsString();

        assertNotNull(responseProductAsJson);
        assertTrue(responseProductAsJson.contains(productA.getName()), "Product name should match");
        assertTrue(responseProductAsJson.contains(String.valueOf(productA.getPrice())), "Product price should match");
    }

    @Test
    @Order(3)
    void test_addNewProduct() throws Exception {
        logger.info("test add() for a new product");

        ProductDTO productDTO = ProductDTO.builder()
                .name(productB.getName())
                .price(productB.getPrice())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(productB);

        MvcResult result = mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertProductToJson(productDTO)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andReturn();

        String expectedResponse = result.getResponse().getContentAsString();

        assertNotNull(expectedResponse);
        assertTrue(expectedResponse.contains(productB.getName()), "Product name should match");
        assertTrue(expectedResponse.contains(String.valueOf(productB.getPrice())), "Product price should match");
    }

    private String convertProductToJson(ProductDTO productDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(productDTO);
        } catch (Exception e) {
            logger.error("convertProductToJson(): {}", e.getMessage());
        }
        return "";
    }
}