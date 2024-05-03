package com.cafe.cafe_management.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cafe.cafe_management.Model.Bill;

public interface BillRepo  extends JpaRepository<Bill , Integer>{

    List<Bill> getAllBills();
    
    List<Bill> getBillByUserName(@Param("username") String username);





}
