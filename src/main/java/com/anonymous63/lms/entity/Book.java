package com.anonymous63.lms.entity;

import com.anonymous63.lms.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "author", "publisher"})
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String category;
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    private int totalCopies;
    private int availableCopies;
    @Column(length = 1000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "added_by")
    private User addedBy;
    @Column(updatable = false)
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active = true;
}
