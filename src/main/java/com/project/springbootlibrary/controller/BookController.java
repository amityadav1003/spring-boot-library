package com.project.springbootlibrary.controller;

import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.responsemodals.ShelfCurrentLoansResponse;
import com.project.springbootlibrary.service.BookService;
import com.project.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.mediatype.alps.Ext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService bookService;

    @Autowired
    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        return bookService.currentLoans(userEmail);
    }
    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId,@RequestHeader(value = "Authorization") String
token) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        return bookService.checkoutBook(userEmail , bookId);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId,@RequestHeader(value = "Authorization") String token){
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        return bookService.checkoutBookByUser(userEmail , bookId);
    }

    @GetMapping("/secure/currentloans/count")
    @Cacheable
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token){
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value="Authorization") String token , @RequestParam Long bookId) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        bookService.returnBook(userEmail , bookId);
    }

    @PostMapping("/secure/renewLoan")
    public void renewLoan(@RequestHeader(value = "Authorization")String token , @RequestParam Long bookId) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        bookService.renewLoan(userEmail , bookId);
    }

}
