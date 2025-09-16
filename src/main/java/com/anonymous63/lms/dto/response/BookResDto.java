package com.anonymous63.lms.dto.response;

import com.anonymous63.lms.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private BookStatus status;
    private int totalCopies;
    private int availableCopies;
    private String description;
    private Long addedByUserId;   // or you can embed UserResponseDTO if needed
    private Instant createdAt;
    private Instant updatedAt;
}
