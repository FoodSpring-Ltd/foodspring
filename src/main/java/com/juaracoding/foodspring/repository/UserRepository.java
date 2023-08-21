package com.juaracoding.foodspring.repository;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/15/2023 9:26 PM
@Last Modified 8/15/2023 9:26 PM
Version 1.0
*/

import com.juaracoding.foodspring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmailOrPhoneOrUsername(String email, String phone, String username);

    List<User> findByEmail(String emails);

    Page<User> findByIsDelete(Pageable pageable, Boolean isDelete);
    List<User> findByIsDelete(Boolean isDelete);

    Page<User> findByIsDeleteAndUserId(Pageable pageable, Boolean isDelete, long parseLong);

    Page<User> findByIsDeleteAndFirstNameContainsIgnoreCase(Pageable pageable, boolean b, String paramValue);

    Page<User> findByIsDeleteAndUsernameContainsIgnoreCase(Pageable pageable, Boolean isDelete, String paramValue);

    Page<User> findByIsDeleteAndEmailContainsIgnoreCase(Pageable pageable, boolean b, String paramValue);

    Page<User> findByIsDeleteAndPhoneContainsIgnoreCase(Pageable pageable, boolean b, String paramValue);
}
