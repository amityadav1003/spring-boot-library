package com.project.springbootlibrary.Dao;

import com.project.springbootlibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface BookDao extends JpaRepository<Book , Long> {
    Page<Book> findByTitleContaining(@RequestParam("title") String title , Pageable pageable);

    Page<Book> findByCategoryContaining(@RequestParam("category")String category , Pageable pageable);


}
