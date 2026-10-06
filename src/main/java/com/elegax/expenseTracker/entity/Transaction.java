package com.elegax.expenseTracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType = TransactionType.EXPENSE;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate dateUpdated;
}
