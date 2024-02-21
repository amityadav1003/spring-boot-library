package com.project.springbootlibrary.Dao;

import com.project.springbootlibrary.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PaymentRepository extends JpaRepository<Payment , Long> {

    Payment findByUserEmail(String userEmail);
}
