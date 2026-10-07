package com.elegax.expenseTracker.dto;

import java.math.BigDecimal;

public record SummaryCalculation(BigDecimal totalIncome, BigDecimal totalExpenses,
                                 BigDecimal balance) {
}
