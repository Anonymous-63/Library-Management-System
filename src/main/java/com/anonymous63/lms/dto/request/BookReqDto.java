package com.anonymous63.lms.dto.request;

import com.anonymous63.lms.enums.BookStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookReqDto {

    private String resourceType;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name must not exceed 255 characters")
    private String author;

    @NotBlank(message = "Publisher is required")
    @Size(max = 255, message = "Publisher name must not exceed 255 characters")
    private String publisher;

    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotNull(message = "Book status is required")
    private BookStatus status;

    @Positive(message = "Total copies must be greater than 0")
    private int totalCopies;

    @Min(value = 0, message = "Available copies cannot be negative")
    @Max(value = 1000, message = "Available copies cannot exceed 1000") // optional safety limit
    private int availableCopies;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Added by user ID is required")
    private Long addedByUserId;  // reference to User who added the book
}
