package com.elegax.expenseTracker.dto;

import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(String transactionId,
                                  TransactionType transactionType,
                                  BigDecimal amount, String description,
                                  Category category, LocalDate dateUpdated) {
}
