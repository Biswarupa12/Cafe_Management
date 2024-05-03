package com.cafe.cafe_management.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.cafe_management.Wrapper.ProductWrapper;

public interface ProductService {
    
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String , String > requestMap);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String>updateStatus(Map<String , String> requeMap);

    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);





}
