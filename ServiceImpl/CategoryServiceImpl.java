package com.cafe.cafe_management.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Filter.JwtFilter;
import com.cafe.cafe_management.Model.Category;
import com.cafe.cafe_management.Repo.CategoryRepo;
import com.cafe.cafe_management.Service.CategoryService;
import com.cafe.cafe_management.utils.CafeUtils;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
/**
 * CategoryServiceImpl
 */
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo categoryRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {

        try {
            if (jwtFilter.isAdmin()) {
                if (validatCategoryMap(requestMap, false)) {
                    categoryRepository.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validatCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {

        Category category = new Category();
        if (isAdd) {
            category.setCategoryId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));

        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equals("true")) {
                log.info("inside if");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);

            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if(validatCategoryMap(requestMap, true)){
               Optional optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                     categoryRepository.save(getCategoryFromMap(requestMap, true));
                     return CafeUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Category id doesnot exist",HttpStatus.OK);
                }
               
            //    return  CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}