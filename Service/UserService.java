package com.cafe.cafe_management.Service;

import java.util.List;
import java.util.Map;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Wrapper.UserWrapper;

// import com.cafe.cafe_management.Repo.UserRepo;


@Service
public interface UserService {
    

      ResponseEntity<String> signUp(Map<String , String> requestMap);

      ResponseEntity<Object> login(Map<String , String> requestMap);
      //<Object nahi String hoga login keliye>
      ResponseEntity <List<UserWrapper>> getAllUser();

      ResponseEntity<String> update(Map<String , String> requestMap);
      
      ResponseEntity<String> checkToken();

      ResponseEntity<String> changePassword(Map<String,String> requestMap);

      ResponseEntity<String> forgotPassword(Map<String,String> requestMap);





}
