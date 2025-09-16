package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.BookTransactionReqDto;
import com.anonymous63.lms.dto.response.BookTransactionResDto;

import java.util.List;

public interface BookTransactionService {
    BookTransactionResDto borrowBook(BookTransactionReqDto reqDto);

    BookTransactionResDto returnBook(Long transactionId);

    BookTransactionResDto getTransactionById(Long transactionId);

    List<BookTransactionResDto> getTransactionsByUser(Long userId);

    List<BookTransactionResDto> getAllTransactions();
}
