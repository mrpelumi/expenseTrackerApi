package com.elegax.expenseTracker.dto;

import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.TransactionType;
import lombok.NonNull;

import java.math.BigDecimal;

public record TransactionRequest(@NonNull TransactionType transactionType,
                                 @NonNull BigDecimal amount, String description,
                                 Category category) {
}
