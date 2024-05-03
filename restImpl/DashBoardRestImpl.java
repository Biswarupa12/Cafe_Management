package com.cafe.cafe_management.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafe_management.Rest.DashBoardRest;
import com.cafe.cafe_management.Service.DashBoardService;

@RestController
public class DashBoardRestImpl  implements DashBoardRest{

    @Autowired
    DashBoardService dashBoardService;




    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        

        return dashBoardService.getCount();
    }
    
}
