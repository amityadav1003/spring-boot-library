package com.project.springbootlibrary.controller;

import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.entity.Review;
import com.project.springbootlibrary.requestmodels.ReviewRequest;
import com.project.springbootlibrary.service.ReviewService;
import com.project.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value = "Authorization") String token , @RequestParam Long bookId) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        if(userEmail == null){
            throw new Exception("User Email is Missing");
        }
        return reviewService.userReviewListed(userEmail , bookId);
    }
    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token , @RequestBody ReviewRequest reviewRequest) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        if(userEmail == null){
            throw new Exception("User Email is Missing");
        }
    reviewService.postReview(userEmail , reviewRequest);
    }


}
