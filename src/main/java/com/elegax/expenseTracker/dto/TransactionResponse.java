package com.elegax.expenseTracker.dto;

import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Transaction Response",
        description = "Transaction response returned by API")
public record TransactionResponse(
        @Schema(description = "Unique transaction ID", example = "TXN-43002")
        String transactionId,
        TransactionType transactionType,
        BigDecimal amount, String description,
        Category category, LocalDate dateUpdated) {
}
