package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
    List<Book> findByActiveTrue();

    Optional<Book> findByIdAndActiveTrue(Long id);

    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublisherIgnoreCase(
            String title, String author, String publisher
    );
}
