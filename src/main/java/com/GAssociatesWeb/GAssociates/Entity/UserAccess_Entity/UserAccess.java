package com.GAssociatesWeb.GAssociates.Entity.UserAccess_Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "profile")
    private String profile;

    @Column(name = "status")
    private String status;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
}
