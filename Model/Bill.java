package com.cafe.cafe_management.Model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @NamedQuery (name = "Bill.getAllBills" , query = "select b from Bill b order by b.id desc")

// @NamedQuery(name = "Bill.getBillByUserName" , query = "select b from Bill where b.createdBy=:username order by b.id desc")




@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "bill")

public class Bill  implements Serializable{
    private static final long serialVersionUID = 1L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name = "contactnumber")
    private  String contactNumber;

    @Column(name="paymentmethod")
    private String paymentMethod;

    @Column(name="total")
    private Integer total;

    @Column(name="productdetails" , columnDefinition = "json")
    private String productDetails;

    @Column(name = "createdby")
    private String createdBy;

}
