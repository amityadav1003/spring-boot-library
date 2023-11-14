package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.BookDao;
import com.project.springbootlibrary.entity.Book;
import com.project.springbootlibrary.responsemodals.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {
    private BookDao bookDao;

    @Autowired
    public AdminService(BookDao bookDao){
    this.bookDao = bookDao;
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
}
