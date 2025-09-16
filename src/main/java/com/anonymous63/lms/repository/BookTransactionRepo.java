package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.BookTransaction;
import com.anonymous63.lms.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookTransactionRepo extends JpaRepository<BookTransaction, Long> {
    List<BookTransaction> findByUserId(Long userId);

    List<BookTransaction> findByStatus(TransactionStatus transactionStatus);
}
