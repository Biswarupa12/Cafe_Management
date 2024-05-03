package com.cafe.cafe_management.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Rest.ProductRest;
import com.cafe.cafe_management.Service.ProductService;
import com.cafe.cafe_management.Wrapper.ProductWrapper;
import com.cafe.cafe_management.utils.CafeUtils;

/**
 * ProductRestImpl
 */
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService  productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            return productService.addNewProduct(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
    
        try {
            return productService.getAllProduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);   
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
            try {
                return productService.updateProduct(requestMap);
            } catch (Exception e) {
                e.printStackTrace();
            }





        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {

        try {
            return productService.deleteProduct(id);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        
        try {
            return productService.updateStatus(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        
        try {
            return productService.getByCategory(id);
        } catch (Exception e) {
            e.printStackTrace();
        }




        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        
        try {
            return productService.getProductById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
}