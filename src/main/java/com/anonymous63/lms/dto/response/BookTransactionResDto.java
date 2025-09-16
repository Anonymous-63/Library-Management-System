package com.anonymous63.lms.dto.response;

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
public class BookTransactionResDto {
    private Long id;
    private Long userId;         // or a nested UserResponseDTO if you want
    private Long bookId;         // or BookResponseDTO
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
}
