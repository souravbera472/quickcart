package com.org.quickcart.controller;

import com.org.quickcart.model.FilterParam;
import com.org.quickcart.entity.Product;
import com.org.quickcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Hello World",HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestBody FilterParam filterParam){
        List<Product> goodsServices = productService.getGoodsServices(filterParam);
        return new ResponseEntity<>(goodsServices,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        System.out.println("Product: " + product.getName());
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Map<String, Object> map){
        return new ResponseEntity<>(productService.updateProduct(id, map), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id){
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }
}
