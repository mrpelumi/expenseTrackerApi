package com.elegax.expenseTracker.dto;

import lombok.NonNull;

import java.math.BigDecimal;

public record TransactionUpdateRequest(@NonNull BigDecimal amount, String description) {
}
