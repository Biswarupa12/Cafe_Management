package com.cafe.cafe_management.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductWrapper {

    Integer id;


    String name;


    String description;


    Integer price;


    String status;


    Integer categoryId;


    String categoryName;

    

   
}
