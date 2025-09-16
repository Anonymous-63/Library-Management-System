package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.BookTransactionReqDto;
import com.anonymous63.lms.dto.response.BookTransactionResDto;
import com.anonymous63.lms.service.BookTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookTransactions")
public class BookTransactionController {
    private final BookTransactionService bookTransactionService;

    // ✅ Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<BookTransactionResDto> borrowBook(
            @RequestBody @Valid BookTransactionReqDto reqDto) {
        BookTransactionResDto transaction = bookTransactionService.borrowBook(reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    // ✅ Return a book
    @PostMapping("/{transactionId}/return")
    public ResponseEntity<BookTransactionResDto> returnBook(@PathVariable Long transactionId) {
        BookTransactionResDto transaction = bookTransactionService.returnBook(transactionId);
        return ResponseEntity.ok(transaction);
    }

    // ✅ Get transaction by ID
    @GetMapping("/{transactionId}")
    public ResponseEntity<BookTransactionResDto> getTransactionById(@PathVariable Long transactionId) {
        BookTransactionResDto transaction = bookTransactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    // ✅ Get all transactions of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookTransactionResDto>> getTransactionsByUser(@PathVariable Long userId) {
        List<BookTransactionResDto> transactions = bookTransactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transactions);
    }

    // ✅ Get all transactions
    @GetMapping
    public ResponseEntity<List<BookTransactionResDto>> getAllTransactions() {
        List<BookTransactionResDto> transactions = bookTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
