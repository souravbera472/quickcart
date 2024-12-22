package com.org.quickcart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    private String category;
    @Id
    private String id;
    private int price;
    private String name;
    private String description;
}
