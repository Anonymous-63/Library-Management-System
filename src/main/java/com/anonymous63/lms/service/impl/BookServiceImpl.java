package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.common.exception.ResourceNotFoundException;
import com.anonymous63.lms.dto.request.BookReqDto;
import com.anonymous63.lms.dto.request.BookSearchReqDto;
import com.anonymous63.lms.dto.response.BookResDto;
import com.anonymous63.lms.entity.Book;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.enums.BookStatus;
import com.anonymous63.lms.mapper.BookMapper;
import com.anonymous63.lms.repository.BookRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper mapper;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    @Override
    public BookResDto addBook(BookReqDto reqDto) {
        boolean exists = bookRepo.existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublisherIgnoreCase(
                reqDto.getTitle(), reqDto.getAuthor(), reqDto.getPublisher()
        );

        if (exists) {
            throw new DataIntegrityViolationException("Book with the same title, author, and publisher already exists");
        }

        Book book = mapper.toEntity(reqDto);

        // set addedBy user (if provided)
        if (reqDto.getAddedByUserId() != null) {
            User user = userRepo.findById(reqDto.getAddedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            book.setAddedBy(user);
        }
        book.setCreatedAt(Instant.now());
        book.setUpdatedAt(Instant.now());
        Book savedBook = bookRepo.save(book);
        return mapper.toDto(savedBook);
    }

    @Override
    public BookResDto updateBook(Long bookId, BookReqDto reqDto) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setTitle(reqDto.getTitle());
        book.setAuthor(reqDto.getAuthor());
        book.setPublisher(reqDto.getPublisher());
        book.setCategory(reqDto.getCategory());
        book.setStatus(reqDto.getStatus());
        book.setTotalCopies(reqDto.getTotalCopies());
        book.setAvailableCopies(reqDto.getAvailableCopies());
        book.setDescription(reqDto.getDescription());
        book.setUpdatedAt(Instant.now());

        Book updated = bookRepo.save(book);
        return mapper.toDto(updated);
    }

    @Override
    public void archiveBook(Long bookId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (!book.isEnabled()) {
            throw new IllegalStateException("Book is already archived");
        }

        book.setEnabled(false);
        book.setUpdatedAt(Instant.now());
        bookRepo.save(book);
    }

    @Override
    public BookResDto getBookById(Long bookId) {
        Book book = bookRepo.findByIdAndEnabledTrue(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found or archived"));
        return mapper.toDto(book);
    }

    @Override
    public List<BookResDto> getAllBooks() {
        return bookRepo.findAll().stream()
                .filter(Book::isEnabled)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResDto> searchBooks(BookSearchReqDto searchDto) {
        return bookRepo.findAll().stream()
                .filter(Book::isEnabled)
                .filter(book ->
                        (searchDto.getTitle() == null || book.getTitle().toLowerCase().contains(searchDto.getTitle().toLowerCase())) &&
                                (searchDto.getAuthor() == null || book.getAuthor().toLowerCase().contains(searchDto.getAuthor().toLowerCase())) &&
                                (searchDto.getCategory() == null || book.getCategory().toLowerCase().contains(searchDto.getCategory().toLowerCase()))
                )
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResDto updateBookStatus(Long bookId, BookStatus status) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setStatus(status);
        book.setUpdatedAt(Instant.now());
        Book updated = bookRepo.save(book);
        return mapper.toDto(updated);
    }

    @Override
    public boolean isBookAvailable(Long bookId) {
        return bookRepo.findById(bookId)
                .filter(Book::isEnabled)
                .map(book -> book.getAvailableCopies() > 0 && book.getStatus() == BookStatus.AVAILABLE)
                .orElse(false);
    }
}
