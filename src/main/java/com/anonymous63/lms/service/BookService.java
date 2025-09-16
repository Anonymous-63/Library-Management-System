package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.BookReqDto;
import com.anonymous63.lms.dto.request.BookSearchReqDto;
import com.anonymous63.lms.dto.response.BookResDto;
import com.anonymous63.lms.enums.BookStatus;

import java.util.List;

public interface BookService {
    BookResDto addBook(BookReqDto reqDto);

    BookResDto updateBook(Long bookId, BookReqDto reqDto);

    void archiveBook(Long bookId);

    BookResDto getBookById(Long bookId);

    List<BookResDto> getAllBooks();

    List<BookResDto> searchBooks(BookSearchReqDto searchDto);

    BookResDto updateBookStatus(Long bookId, BookStatus status);

    boolean isBookAvailable(Long bookId);

}
