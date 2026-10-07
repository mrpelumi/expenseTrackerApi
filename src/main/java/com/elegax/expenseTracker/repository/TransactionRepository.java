package com.elegax.expenseTracker.repository;

import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.Transaction;
import com.elegax.expenseTracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop5ByOrderByDateUpdatedDesc();

    List<Transaction> findByCategoryAndTransactionTypeOrderByDateUpdatedDesc(Category category,
                                                                             TransactionType transactionType);

    List<Transaction> findByTransactionTypeOrderByDateUpdatedDesc(TransactionType transactionType);

    List<Transaction> findByCategoryOrderByDateUpdatedDesc(Category category);

    Optional<Transaction> findByTransactionIdOrderByDateUpdatedDesc(String transactionId);

    long deleteByTransactionId(String transactionId);


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.transactionType = :transactionType
            AND t.dateUpdated BETWEEN :startDate AND :endDate
            """)
    BigDecimal calculateTotal(@Param("transactionType") TransactionType transactionType,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);
}