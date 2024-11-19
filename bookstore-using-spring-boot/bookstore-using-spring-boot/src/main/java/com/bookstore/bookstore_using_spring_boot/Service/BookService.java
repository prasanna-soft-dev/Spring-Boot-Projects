package com.bookstore.bookstore_using_spring_boot.Service;

import com.bookstore.bookstore_using_spring_boot.Entity.Book;

import java.util.List;
public interface BookService {
    Book createBook(Book book);
    Book getBookById(Long bookId);
    List<Book> getAllBooks();
    Book updateBook(Book book);
    void deleteBook(Long bookId);

}