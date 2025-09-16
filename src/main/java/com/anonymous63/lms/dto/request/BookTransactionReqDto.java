package com.anonymous63.lms.dto.request;

import com.anonymous63.lms.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTransactionReqDto {
    private Long userId;         // Who is borrowing/returning
    private Long bookId;         // Which book
    private LocalDate borrowDate;  // When borrowed
    private LocalDate dueDate;     // When it should be returned
    private LocalDate returnDate;  // Optional, only for return
    private TransactionStatus status;
}
