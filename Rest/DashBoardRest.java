package com.cafe.cafe_management.Rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dashboard")
public interface DashBoardRest {
    

    @GetMapping("/details")
    ResponseEntity<Map<String , Object>> getCount();




}
