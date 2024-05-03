package com.cafe.cafe_management.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.cafe.cafe_management.Model.Product;
import com.cafe.cafe_management.Wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

public interface ProductRepo  extends JpaRepository<Product , Integer>{

    List<ProductWrapper> getAllProduct();


    @Modifying
    @Transactional
     Integer updateProductStatus (@Param("'status'") String status ,@Param ("id") Integer id );

     List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

     ProductWrapper getProductById(@Param("id") Integer id);

    
}
