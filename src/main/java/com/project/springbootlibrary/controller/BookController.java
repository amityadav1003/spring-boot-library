package com.project.springbootlibrary.controller;

import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.service.BookService;
import com.project.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;

    @Autowired
    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId,@RequestHeader(value = "Authorization") String token) throws Exception{
        String userEmail = "amityadav779@gmail.com";
        return bookService.checkoutBook(userEmail , bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId,@RequestHeader(value = "Authorization") String token){
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "sub");
        return bookService.checkoutBookByUser(userEmail , bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token){
        String userEmail = "amityadav779@gmail.com";
        return bookService.currentLoansCount(userEmail);
    }


}
