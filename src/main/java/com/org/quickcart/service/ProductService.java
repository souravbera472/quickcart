package com.org.quickcart.service;

import com.org.quickcart.exception.ProductAlreadyExistException;
import com.org.quickcart.exception.ProductNotFoundException;
import com.org.quickcart.model.FilterParam;
import com.org.quickcart.model.Product;
import com.org.quickcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
            throw new ProductAlreadyExistException(product.getName());
        }
        return productRepository.save(product);
    }

    public Product getProductById(String id){

        Optional<Product> tempProduct = productRepository.findById(id);
        if(tempProduct.isEmpty()){
            throw new ProductNotFoundException(id);
        }

        return tempProduct.get();
    }

    public Product updateProduct(String id, Map<String , Object> map){

        Optional<Product> tempProduct = productRepository.findById(id);
        if(tempProduct.isEmpty()){
            throw new ProductNotFoundException(id);
        }

        if(map.containsKey("name")){
            if(productRepository.findByName(map.get("name").toString()).isPresent()){
                throw new ProductAlreadyExistException(map.get("name").toString());
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

        return productRepository.save(tempProduct.get());
    }

    public String deleteProduct(String id){

        if(productRepository.findById(id).isEmpty()){
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        return "Product delete successfully";
    }
}
