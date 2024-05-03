package com.cafe.cafe_management.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cafe.cafe_management.Model.User;
// import java.util.List;
import com.cafe.cafe_management.Wrapper.UserWrapper;


public interface UserRepo extends JpaRepository<User , Integer> {

    User  findByEmailId(@Param("email")String email);
    // User  findById(String email);
    // User  findById(String email);
    

    List<UserWrapper> getAllUser();


    List<String>getAllAdmin();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

    User findByemail(String email);

    
}

