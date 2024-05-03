package com.cafe.cafe_management.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafe_management.Model.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> getAllCategory();
    
}
