package com.cafe.cafe_management.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe.cafe_management.Model.Category;

@RequestMapping("/category")
public interface CategoryRest {
    
    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required =  true)Map<String , String> requestMap);

    @GetMapping("/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String FilterValue);

    @PostMapping("/upate")
    ResponseEntity<String>updateCategory(@RequestBody(required = true)Map<String , String> requestMap);




}
