package com.juaracoding.foodspring.model;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/13/2023 11:10 PM
@Last Modified 8/13/2023 11:10 PM
Version 1.0
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MstUser")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userId;

    @Column(name = "FirstName")
    @NotNull(message = "FirstName can't be null")
    @NotEmpty(message = "FirstName can't be empty")
    @NotBlank(message = "FirstName can't be blank")
    @Length(message = "Firstname max length 50", max = 50)
    private String firstName;

    @Column(name = "LastName")
    @Length(message = "Lastname max length 50", max = 50)
    private String lastName;

    @Column(name = "Username", unique = true)
    @NotNull(message = "Username can't be null")
    @NotEmpty(message = "Username can't be empty")
    @NotBlank(message = "Username can't be blank")
    @Length(message = "Username max length 50", max = 50)
    private String username;

    @Column(name = "Email", unique = true)
    @NotNull(message = "Email can't be null")
    @NotEmpty(message = "Email can't be empty")
    @NotBlank(message = "Email can't be blank")
    @Email
    private String email;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Password")
    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")
    @NotBlank(message = "Password can't be blank")
    @Length(message = "Password min length 6", min = 6)
    private String password;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    private List<ShoppingCart> shoppingCarts;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
    private List<ShopOrder> shopOrders;


    @Column(name = "Birthday")
    private LocalDate birthDay;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "Token")
    private String token;

    @Column(name = "TokenCounter")
    private Integer tokenCounter=0;

    @Column(name = "PasswordCounter")
    private Integer passwordCounter=0;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "CreatedAt", columnDefinition = "datetime2 default getdate()")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", columnDefinition = "datetime2 default getdate()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "IsDelete", columnDefinition = "smallint default 0")
    private Boolean isDelete = false;

    @Column(name = "IsAdmin", columnDefinition = "smallint default 0")
    private Boolean isAdmin = false;
}
