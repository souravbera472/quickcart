package com.org.quickcart.service;

import com.org.quickcart.model.FilterParam;
import com.org.quickcart.entity.Product;
import com.org.quickcart.repository.QuickServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuickCartService {
    @Autowired
    private QuickServiceRepository quickServiceRepository;
    public List<Product> getGoodsServices(FilterParam filterParam){
        // todo use limit offset
       return quickServiceRepository.findAll();
    }
}
