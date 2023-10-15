package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.BookDao;
import com.project.springbootlibrary.Dao.CheckoutDao;
import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.entity.Checkout;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class BookService {


    private BookDao bookDao;

    private CheckoutDao checkoutDao;
    @Autowired
    BookService(BookDao bookDao , CheckoutDao checkoutDao) {
        this.bookDao = bookDao;
        this.checkoutDao = checkoutDao;
    }

    public Book checkoutBook(String userEmail , Long bookId) throws Exception{
        Optional<Book> book = bookDao.findById(bookId);

        Checkout validateCheckout = checkoutDao.findByUserEmailAndBookId(userEmail , bookId);

        if(!book.isPresent() || validateCheckout!=null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book Doesn't exists or its already checked out by User");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookDao.save(book.get());

        Checkout checkout = new Checkout(userEmail , LocalDate.now().toString() , LocalDate.now().plusDays(7).toString() , book.get().getId());
        checkoutDao.save(checkout);
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail , Long bookId){
        Checkout validateCheckout = checkoutDao.findByUserEmailAndBookId(userEmail , bookId);
        if(validateCheckout != null){
            return true;
        }
        else{
            return false;
        }
    }

    public int currentLoansCount(String userEmail){
        return checkoutDao.findBookByUserEmail(userEmail).size();
    }


}


































