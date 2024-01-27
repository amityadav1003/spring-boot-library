package com.project.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "checkout")
@Data
public class Checkout {
    public Checkout(String userEmail , String checkoutDate , String returnedDate , Long bookId){
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnedDate = returnedDate;
        this.bookId = bookId;

    }
    public Checkout(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "user_email")
    private String userEmail;


    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "return_date")
    private String returnedDate;

    @Column(name = "book_id")
    private Long bookId;

}
