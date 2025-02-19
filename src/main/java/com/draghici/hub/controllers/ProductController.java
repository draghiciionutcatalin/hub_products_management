package com.draghici.hub.controllers;

import com.draghici.hub.beans.Product;
import com.draghici.hub.dto.ProductDTO;
import com.draghici.hub.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get the list of products. Auth required",
            description = "Returns the list of products",
            security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("/all")
    List<Product> listProduct() {
        return productService.getAll();
    }

    @Operation(
            summary = "Get a product. Auth required. Only admin role",
            description = "Get product by id",
            security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("/{id}")
    Product getOne(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @Operation(
            summary = "Add product. Auth required. Only admin role",
            description = "Add a new product",
            security = @SecurityRequirement(name = "basicAuth"))
    @PostMapping()
    Product add(@RequestBody ProductDTO productDto) {
        return productService.add(productDto);
    }

    @Operation(
            summary = "Update product. Auth required. Only admin role",
            description = "Update existing product",
            security = @SecurityRequirement(name = "basicAuth"))
    @PatchMapping("/{id}")
    Product update(@PathVariable("id") Long id, @RequestBody ProductDTO productDto) {
        return productService.update(id, productDto);
    }

    @Operation(
            summary = "Delete product. Auth required. Only admin role",
            description = "Delete existing product",
            security = @SecurityRequirement(name = "basicAuth"))
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable("id") Long id) {
        productService.delete(id);
    }

}