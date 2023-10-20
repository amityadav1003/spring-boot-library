package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.BookDao;
import com.project.springbootlibrary.Dao.ReviewDao;
import com.project.springbootlibrary.entity.Review;
import com.project.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {
    private ReviewDao reviewDao;


    @Autowired
    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public void postReview(String userEmail , ReviewRequest reviewRequest) throws Exception{
        Review validateReview = reviewDao.findByUserEmailAndBookId(userEmail , reviewRequest.getBookId());
        if(validateReview != null){
            throw new Exception("Review already created");
        }
        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        if(reviewRequest.getReviewDescription().isPresent()){
            review.setReviewDescription(reviewRequest.getReviewDescription().toString());
        }
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewDao.save(review);
    }
    public Boolean userReviewListed(String userEmail , Long bookId){
        Review validateReview = reviewDao.findByUserEmailAndBookId(userEmail , bookId);
        if(validateReview!=null){
            return true;
        }
        return  false;
    }

}
