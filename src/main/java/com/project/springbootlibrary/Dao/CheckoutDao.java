package com.project.springbootlibrary.Dao;

import com.project.springbootlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutDao extends JpaRepository<Checkout , Long>{

    Checkout findByUserEmailAndBookId(String userEmail , Long bookId);


    List<Checkout> findBookByUserEmail(String userEmail);

}
