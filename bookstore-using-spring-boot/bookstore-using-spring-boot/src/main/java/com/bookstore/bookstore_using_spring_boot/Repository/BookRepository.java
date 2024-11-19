package com.bookstore.bookstore_using_spring_boot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bookstore.bookstore_using_spring_boot.Entity.Book;
public interface BookRepository extends JpaRepository<Book, Long> {

}
