package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.BookDao;
import com.project.springbootlibrary.Dao.CheckoutDao;
import com.project.springbootlibrary.Dao.ReviewDao;
import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.entity.Review;
import com.project.springbootlibrary.responsemodals.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Transactional
@Service
public class AdminService {
    private BookDao bookDao;
    private CheckoutDao checkoutDao;
    private ReviewDao reviewDao;

    @Autowired
    public AdminService(BookDao bookDao , ReviewDao reviewDao , CheckoutDao checkoutDao){
    this.bookDao = bookDao;
    this.reviewDao = reviewDao;
    this.checkoutDao = checkoutDao;
    }

    public void increaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookDao.findById(bookId);
        if(!book.isPresent()){
            throw new Exception("Book was not Found or quantity Locked");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);
        bookDao.save(book.get());
    }

    public void decreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookDao.findById(bookId);
        if(!book.isPresent() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0){
            throw new Exception("Book was not Found");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);
        bookDao.save(book.get());
    }

    public void postBook(AddBookRequest addBookRequest){
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCopies(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());
        bookDao.save(book);
    }

    public void deleteBook(Long bookId) throws Exception{

        Optional<Book> book = bookDao.findById(bookId);
        if(!book.isPresent()){
            throw new Exception("Book not found");
        }
        bookDao.delete(book.get());
        reviewDao.deleteAllByBookId(bookId);
        checkoutDao.deleteAllByBookId(bookId);
    }
}
