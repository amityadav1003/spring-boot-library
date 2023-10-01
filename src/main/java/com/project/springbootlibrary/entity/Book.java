package com.project.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name="description")
    private String description;
    @Column(name = "copies")
    private int copies;
    @Column(name="copies_available")
    private int copiesAvailable;
    @Column(name="category")
    private String category;
    @Column(name = "img")
    private String img;

}
