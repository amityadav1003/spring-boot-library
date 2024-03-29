package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.BookDao;
import com.project.springbootlibrary.Dao.CheckoutDao;
import com.project.springbootlibrary.Dao.HistoryRepository;
import com.project.springbootlibrary.Dao.PaymentRepository;
import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.entity.Checkout;
import com.project.springbootlibrary.entity.History;
import com.project.springbootlibrary.entity.Payment;
import com.project.springbootlibrary.responsemodals.ShelfCurrentLoansResponse;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {


    private BookDao bookDao;

    private CheckoutDao checkoutDao;
    private HistoryRepository historyRepository;

    private PaymentRepository paymentRepository;
    @Autowired
    BookService(BookDao bookDao , CheckoutDao checkoutDao , HistoryRepository historyRepository , PaymentRepository paymentRepository) {
        this.bookDao = bookDao;
        this.checkoutDao = checkoutDao;
        this.historyRepository = historyRepository;
        this.paymentRepository = paymentRepository;
    }

    public Book checkoutBook(String userEmail , Long bookId) throws Exception{
        Optional<Book> book = bookDao.findById(bookId);

        Checkout validateCheckout = checkoutDao.findByUserEmailAndBookId(userEmail , bookId);

        if(!book.isPresent() || validateCheckout!=null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book Doesn't exists or its already checked out by User");
        }
        List<Checkout> currentBooksCheckout = checkoutDao.findBookByUserEmail(userEmail);
        boolean bookNeedsReturned = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Checkout checkout : currentBooksCheckout){
            Date d1 = sdf.parse(checkout.getReturnedDate());
            Date d2 = sdf.parse(LocalDate.now().toString());
            TimeUnit time = TimeUnit.DAYS;
            double timeUnitDifference = time.convert(d1.getTime() - d2.getTime() , TimeUnit.MILLISECONDS);
            if (timeUnitDifference < 0){
                bookNeedsReturned = true;
                break;
            }
        }
        Payment userPayment = paymentRepository.findByUserEmail(userEmail);
        if(userPayment!=null && userPayment.getAmount() > 0 || userPayment!=null && bookNeedsReturned ){
            throw  new Exception("OutStanding Fees");
        }
        if(userPayment == null){
            Payment payment = new Payment();
            payment.setAmount(0.00);
            payment.setUserEmail(userEmail);
            paymentRepository.save(payment);
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
    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws  Exception{
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<ShelfCurrentLoansResponse>();
        List<Checkout> checkoutList = checkoutDao.findBookByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();
        for(Checkout i : checkoutList){
            bookIdList.add(i.getBookId());
        }
        List<Book> books = bookDao.findBooksByBooksId(bookIdList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Book book : books){
            Optional<Checkout> checkout = checkoutList.stream().filter(x-> x.getBookId() == book.getId()).findFirst();
        if(checkout.isPresent()){
            Date d1 = sdf.parse(checkout.get().getReturnedDate());
            Date d2 = sdf.parse(LocalDate.now().toString());
            TimeUnit time = TimeUnit.DAYS;
            long differenceInTime =time.convert(d1.getTime() - d2.getTime() , TimeUnit.MILLISECONDS);
            shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book , (int) differenceInTime));
        }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail , Long bookId) throws Exception{
        Optional<Book> book = bookDao.findById(bookId);
        Checkout validateCheckout = checkoutDao.findByUserEmailAndBookId(userEmail , bookId);
        if(!book.isPresent() || validateCheckout == null){
            throw new Exception("Book doesn't Exist or not checked out by user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookDao.save(book.get());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validateCheckout.getReturnedDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        TimeUnit time = TimeUnit.DAYS;

        double differenceInTime = time.convert(d2.getTime() - d1.getTime() , TimeUnit.MILLISECONDS);
        if(differenceInTime < 0 ){
            Payment payment = paymentRepository.findByUserEmail(userEmail);
            payment.setAmount(payment.getAmount() + (differenceInTime * -1));
            paymentRepository.save(payment);
        }
        checkoutDao.deleteById(validateCheckout.getId());
        History history = new History(
                userEmail, validateCheckout.getCheckoutDate() , LocalDate.now().toString(),book.get().getTitle() , book.get().getAuthor(),book.get().getDescription(),
                book.get().getImg()
        );
        historyRepository.save(history);
    }

    public void renewLoan(String userEmail , Long bookId) throws Exception {
        Checkout validateCheckout = checkoutDao.findByUserEmailAndBookId(userEmail , bookId);
        if(validateCheckout == null){
            throw new Exception("Book doesn't exist or not checked out by user");
        }
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdFormat.parse(validateCheckout.getReturnedDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());
        if(d1.compareTo(d2) >=0){
            validateCheckout.setReturnedDate(LocalDate.now().plusDays(7).toString());
            checkoutDao.save(validateCheckout);
        }
    }



}


































