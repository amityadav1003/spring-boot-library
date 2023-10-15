package com.project.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Entity
@Table(name = "review")
@Data
public class Review {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "date")
    @CreationTimestamp
    private Date date;
    @Column(name ="rating")
    private double rating;
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "review_description")
    private String reviewDescription;
}
