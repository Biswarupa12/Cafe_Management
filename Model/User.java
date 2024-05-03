package com.cafe.cafe_management.Model;

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




@AllArgsConstructor
@NoArgsConstructor
// @NamedQuery(name = "User.findByEmailId",query = "select u from User u where u.email=:email  " )

// @NamedQuery(name = "User.getAllUser", query = "select new com.cafe.cafe_management.Wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'  ")

// @NamedQuery(name = "User.updateStatus" , query = "update User u set u.status=:status where u.id=:id")

// @NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'  ")
@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User {
    // private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="contactNumber")
    private String contactNumber;

    @Column(name="email")
    private String email;

    @Column(name="passord")
    private  String password;

    @Column(name="status")
    private Boolean status;

    @Column(name="role")
    private String role;
}
