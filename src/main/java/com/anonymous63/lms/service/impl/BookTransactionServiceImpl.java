package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.common.exception.ResourceNotFoundException;
import com.anonymous63.lms.dto.request.BookTransactionReqDto;
import com.anonymous63.lms.dto.response.BookTransactionResDto;
import com.anonymous63.lms.entity.Book;
import com.anonymous63.lms.entity.BookTransaction;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.enums.BookStatus;
import com.anonymous63.lms.enums.TransactionStatus;
import com.anonymous63.lms.mapper.BookTransactionMapper;
import com.anonymous63.lms.repository.BookRepo;
import com.anonymous63.lms.repository.BookTransactionRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.service.BookTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {
    private final BookTransactionRepo bookTransactionRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;
    private final BookTransactionMapper mapper;

    @Override
    public BookTransactionResDto borrowBook(BookTransactionReqDto reqDto) {
        // find user
        User user = userRepo.findById(reqDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // find book
        Book book = bookRepo.findById(reqDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0 || book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // create transaction
        BookTransaction transaction = new BookTransaction();
        transaction.setUser(user);
        transaction.setBook(book);
        transaction.setBorrowDate(reqDto.getBorrowDate() != null ? reqDto.getBorrowDate() : LocalDate.now());
        transaction.setDueDate(reqDto.getDueDate() != null ? reqDto.getDueDate() : transaction.getBorrowDate().plusDays(7)); // default 1 week
        transaction.setStatus(TransactionStatus.BORROWED);

        // update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        BookTransaction saved = bookTransactionRepo.save(transaction);
        return mapper.toDto(saved);
    }

    @Override
    public BookTransactionResDto returnBook(Long transactionId) {
        BookTransaction transaction = bookTransactionRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("Book is already returned");
        }

        // update transaction
        transaction.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(transaction.getDueDate()))
            transaction.setStatus(TransactionStatus.OVERDUE);
        else
            transaction.setStatus(TransactionStatus.RETURNED);

        // update book availability
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);

        BookTransaction updated = bookTransactionRepo.save(transaction);
        return mapper.toDto(updated);
    }

    @Override
    public BookTransactionResDto getTransactionById(Long transactionId) {
        BookTransaction transaction = bookTransactionRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return mapper.toDto(transaction);
    }

    @Override
    public List<BookTransactionResDto> getTransactionsByUser(Long userId) {
        return bookTransactionRepo.findByUserId(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookTransactionResDto> getAllTransactions() {
        return bookTransactionRepo.findAll().stream()
                .map(txn -> {
                    // Recalculate status dynamically
                    TransactionStatus newStatus = calculateStatus(txn);

                    if (newStatus != txn.getStatus()) {
                        txn.setStatus(newStatus);
                        bookTransactionRepo.save(txn); // persist the update
                    }

                    return mapper.toDto(txn);
                })
                .collect(Collectors.toList());
    }

    public TransactionStatus calculateStatus(BookTransaction txn) {
        LocalDate now = LocalDate.now();

        if (txn.getStatus() == TransactionStatus.RETURNED) {
            return TransactionStatus.RETURNED;
        }
        if (txn.getDueDate().isBefore(now)) {
            return TransactionStatus.OVERDUE;
        }
        return TransactionStatus.BORROWED;
    }
}
