package com.org.quickcart.controller;

import com.org.quickcart.logger.QLogger;
import com.org.quickcart.model.FilterParam;
import com.org.quickcart.entity.Product;
import com.org.quickcart.service.QuickCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class QuickCartController {
    @Autowired
    private QuickCartService quickCartService;

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        QLogger.info("This is Logger...");
        return new ResponseEntity<>("Hello World",HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestBody FilterParam filterParam){
        List<Product> goodsServices = quickCartService.getGoodsServices(filterParam);
        return new ResponseEntity<>(goodsServices,HttpStatus.OK);
    }
}
