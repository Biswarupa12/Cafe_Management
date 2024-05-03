package com.cafe.cafe_management.Config;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Repo.UserRepo;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j

public class CostumUserDetailsService  implements UserDetailsService{

    @Autowired
    UserRepo  userRepo;
    private com.cafe.cafe_management.Model.User userDetail;
    


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside LoadUserByUsername{}",username);    
        // userDetail = userRepo.findById(username);
        userDetail = userRepo.findByEmailId(username);

        // if(!Objects.isNull(userDetail))
        if(Objects.isNull(userDetail))
            return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
         else{

            throw new UsernameNotFoundException("User not found");
        }
        }

    public  com.cafe.cafe_management.Model.User  getUserDetail(){
        return  userDetail;
        
    }

  
    

}
