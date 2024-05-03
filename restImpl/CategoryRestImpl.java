package com.cafe.cafe_management.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Model.Category;
import com.cafe.cafe_management.Rest.CategoryRest;
import com.cafe.cafe_management.Service.CategoryService;
import com.cafe.cafe_management.utils.CafeUtils;

@RestController
public class CategoryRestImpl implements  CategoryRest{


    @Autowired
    CategoryService  categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
       
        try {
            return categoryService.addNewCategory(requestMap);
            
        } catch (Exception e) {
            e.printStackTrace();
        }


        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    public ResponseEntity<List<Category>> getAllCategory(String FilterValue) {
        try {
            
            return categoryService.getAllCategory(FilterValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
     
        try{
            return categoryService.updateCategory(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    
}
