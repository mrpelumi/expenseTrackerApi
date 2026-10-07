package com.elegax.expenseTracker.dto;

import java.math.BigDecimal;
import java.util.List;

public record SummaryResponse(SummaryCalculation summaryCalculation,
                              List<TransactionResponse> recentTransactions) {
}
