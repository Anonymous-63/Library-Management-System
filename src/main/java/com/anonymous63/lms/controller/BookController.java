package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.BookReqDto;
import com.anonymous63.lms.dto.request.BookSearchReqDto;
import com.anonymous63.lms.dto.response.BookResDto;
import com.anonymous63.lms.enums.BookStatus;
import com.anonymous63.lms.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    // ✅ Add a new book
    @PostMapping
    public ResponseEntity<BookResDto> addBook(@RequestBody @Valid BookReqDto reqDto) {
        BookResDto createdBook = bookService.addBook(reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    // ✅ Update an existing book
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResDto> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookReqDto reqDto) {
        BookResDto updatedBook = bookService.updateBook(bookId, reqDto);
        return ResponseEntity.ok(updatedBook);
    }

    // ✅ Archive a book (soft delete)
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> archiveBook(@PathVariable Long bookId) {
        bookService.archiveBook(bookId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get book by ID
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResDto> getBookById(@PathVariable Long bookId) {
        BookResDto book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    // ✅ Get all active books
    @GetMapping
    public ResponseEntity<List<BookResDto>> getAllBooks() {
        List<BookResDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // ✅ Search books with filters
    @PostMapping("/search")
    public ResponseEntity<List<BookResDto>> searchBooks(@RequestBody BookSearchReqDto searchDto) {
        List<BookResDto> books = bookService.searchBooks(searchDto);
        return ResponseEntity.ok(books);
    }

    // ✅ Update book status (AVAILABLE, BORROWED, etc.)
    @PatchMapping("/{bookId}/status")
    public ResponseEntity<BookResDto> updateBookStatus(
            @PathVariable Long bookId,
            @RequestParam BookStatus status) {
        BookResDto book = bookService.updateBookStatus(bookId, status);
        return ResponseEntity.ok(book);
    }

    // ✅ Check availability
    @GetMapping("/{bookId}/availability")
    public ResponseEntity<Boolean> isBookAvailable(@PathVariable Long bookId) {
        boolean available = bookService.isBookAvailable(bookId);
        return ResponseEntity.ok(available);
    }

}
