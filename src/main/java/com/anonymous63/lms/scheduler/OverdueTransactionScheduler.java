package com.anonymous63.lms.scheduler;

import com.anonymous63.lms.entity.BookTransaction;
import com.anonymous63.lms.enums.TransactionStatus;
import com.anonymous63.lms.repository.BookTransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueTransactionScheduler {
    private final BookTransactionRepo bookTransactionRepo;

    @Value("${app.scheduler.overdue-cron}")
    private String overdueCron;

    @Scheduled(cron = "${app.scheduler.overdue-cron}", zone = "Asia/Kolkata")
    public void markOverdueTransactions() {
        LocalDate now = LocalDate.now();
        List<BookTransaction> borrowedTransactions = bookTransactionRepo.findByStatus(TransactionStatus.BORROWED);

        for (BookTransaction txn : borrowedTransactions) {
            if (txn.getDueDate().isBefore(now)) {
                txn.setStatus(TransactionStatus.OVERDUE);
            }
        }

        if (!borrowedTransactions.isEmpty()) {
            bookTransactionRepo.saveAll(borrowedTransactions);
        }
    }
}
