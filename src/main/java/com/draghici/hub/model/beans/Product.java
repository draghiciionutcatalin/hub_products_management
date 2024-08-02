package com.draghici.hub.model.beans;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "P_PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "price", nullable = false)
    double price;
}
