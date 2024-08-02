package com.draghici.hub.controllers;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get the list of products. Auth required",
            description = """
            Returns the list of products in paginated format. The endpoint supports pagination parameters like page number, page size and sort by field. \n
            ex: {"page": 0, "size": 10, "sort": ["id,desc"]} \n
            "id,desc" -> means descending sort by property id \n
            """,
            security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping
    Page<Product> listProduct(Pageable pageable) {
        return productService.getAll(pageable);
    }

    @Operation(
            summary = "Get a product. Auth required",
            description = "Get product by id",
            security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("/{id}")
    Product getOne(@PathVariable("id") Long id) throws Exception {
        return productService.getOne(id);
    }



}

