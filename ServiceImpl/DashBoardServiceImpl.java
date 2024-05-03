package com.cafe.cafe_management.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Repo.BillRepo;
import com.cafe.cafe_management.Repo.CategoryRepo;
import com.cafe.cafe_management.Repo.ProductRepo;
import com.cafe.cafe_management.Service.DashBoardService;

@Service
public class DashBoardServiceImpl   implements DashBoardService{
    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    BillRepo billRepo;





    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String ,  Object> map = new HashMap<>();
        map.put("category", categoryRepo.count());
        map.put("product", productRepo.count());
        map.put("bill", billRepo.count());
        

        return new ResponseEntity<>(map , HttpStatus.OK);
    }
    
}
