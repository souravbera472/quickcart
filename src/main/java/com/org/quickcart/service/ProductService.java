package com.org.quickcart.service;

import com.org.quickcart.exception.CustomException;
import com.org.quickcart.exception.ErrorCode;
import com.org.quickcart.logger.QLogger;
import com.org.quickcart.model.FilterParam;
import com.org.quickcart.entity.Product;
import com.org.quickcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getGoodsServices(FilterParam filterParam){
        // todo use limit offset
       return productRepository.findAll();
    }

    public Product addProduct(Product product){
        Optional<Product> tempProduct = productRepository.findByName(product.getName());
        if(tempProduct.isPresent()){
            QLogger.warn("Product is already exist.");
            throw new CustomException(ErrorCode.PRODUCT_ALREADY_EXIST);
        }

        QLogger.info("Product added successfully.");
        return productRepository.save(product);
    }

    public Product getProductById(String id){
        Optional<Product> tempProduct = productRepository.findById(id);
        if(tempProduct.isEmpty()){
            QLogger.warn("Product not available.");
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return tempProduct.get();
    }

    public Product updateProduct(String id, Map<String , Object> map){
        Optional<Product> tempProduct = productRepository.findById(id);
        if(tempProduct.isEmpty()){
            QLogger.error("Product not found.");
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        if(map.containsKey("name")){
            if(productRepository.findByName(map.get("name").toString()).isPresent()){
                QLogger.error("Product already exist.");
                throw new CustomException(ErrorCode.PRODUCT_ALREADY_EXIST);
            }
            tempProduct.get().setName(map.get("name").toString());
        }

        if(map.containsKey("description")){
            tempProduct.get().setDescription(map.get("description").toString());
        }

        if(map.containsKey("category")){
            tempProduct.get().setCategory(map.get("category").toString());
        }

        if(map.containsKey("price")){
            tempProduct.get().setPrice(Double.parseDouble(map.get("price").toString()));
        }

        QLogger.info("Product updated successfully.");
        return productRepository.save(tempProduct.get());
    }

    public String deleteProduct(String id){
        if(productRepository.findById(id).isEmpty()){
            QLogger.error("Product not found.");
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(id);
        QLogger.info("Product deleted successfully.");
        return "Product delete successfully";
    }
}
