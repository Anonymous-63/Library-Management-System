package com.anonymous63.lms.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSearchReqDto {

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Size(max = 150, message = "Author name must not exceed 150 characters")
    private String author;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
}
