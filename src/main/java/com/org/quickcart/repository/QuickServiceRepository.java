package com.org.quickcart.repository;

import com.org.quickcart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuickServiceRepository extends JpaRepository<Product,String> {

}
