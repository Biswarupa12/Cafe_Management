package com.cafe.cafe_management.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.cafe_management.Wrapper.UserWrapper;


@RequestMapping("/user")
public interface UserRest {
    
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true)Map<String,String> requestMap);
    // public ResponseEntity<String> signUp(@RequestBody(required = true)Map<String,String> requestMap);



    @PostMapping("/login")
    public  ResponseEntity<Object> login(@RequestBody(required = true )Map<String,String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> requestMap);
    

    @GetMapping("/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping("/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);







}
