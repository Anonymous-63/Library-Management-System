package com.anonymous63.lms.dto.request;

import com.anonymous63.lms.enums.TransactionStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTransactionReqDto {

    @NotNull(message = "User ID is required")
    private Long userId;         // Who is borrowing/returning

    @NotNull(message = "Book ID is required")
    private Long bookId;         // Which book

    @NotNull(message = "Borrow date is required")
    @PastOrPresent(message = "Borrow date cannot be in the future")
    private LocalDate borrowDate;  // When borrowed

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;     // When it should be returned

    private LocalDate returnDate;  // Optional, only for return

    @NotNull(message = "Transaction status is required")
    private TransactionStatus status;
}
