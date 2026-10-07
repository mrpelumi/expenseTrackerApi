package com.elegax.expenseTracker.services;

import com.elegax.expenseTracker.dto.*;
import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.SummaryFilter;
import com.elegax.expenseTracker.entity.Transaction;
import com.elegax.expenseTracker.entity.TransactionType;
import com.elegax.expenseTracker.mappers.TransactionRequestMapper;
import com.elegax.expenseTracker.mappers.TransactionResponseMapper;
import com.elegax.expenseTracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionResponseMapper transactionResponseMapper;
    private final TransactionRequestMapper transactionRequestMapper;

    /**
     *
     * @param category
     * @param transactionType
     * @return transactions filtered by category and/or transaction type, ordered by date updated in descending order.
     * If both parameters are null, returns all transactions ordered by date updated in descending order.
     */
    public List<TransactionResponse> findAll(Category category, TransactionType transactionType){
        if (category != null && transactionType != null){
            return transactionRepository.findByCategoryAndTransactionTypeOrderByDateUpdatedDesc(category, transactionType)
                    .stream().map(transactionResponseMapper::toDto)
                    .toList();
        } else if (category != null) {
            return transactionRepository.findByCategoryOrderByDateUpdatedDesc(category)
                    .stream().map(transactionResponseMapper::toDto)
                    .toList();
        } else if (transactionType != null) {
            return transactionRepository.findByTransactionTypeOrderByDateUpdatedDesc(transactionType)
                    .stream().map(transactionResponseMapper::toDto)
                    .toList();
        }

        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "dateUpdated"))
                .stream().map(transactionResponseMapper::toDto)
                .toList();
    }

    public TransactionResponse findById(Long id){
        return transactionRepository.findById(id)
                .map(transactionResponseMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    public TransactionResponse findByTransactionId(String transactionId){
        return transactionRepository.findByTransactionIdOrderByDateUpdatedDesc(transactionId)
                .map(transactionResponseMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    public void createTransaction(TransactionRequest transactionRequest){
        Transaction transaction = transactionRequestMapper.toEntity(transactionRequest);
        transaction.setTransactionId(generateTransactionNumber());
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(String transactionId){
        transactionRepository.deleteByTransactionId(transactionId);
    }

    /* ================= FIND SUMMARY ================= */

    /**
     *
     * @param filterBy
     * @return SummaryResponse record holding the balance, totalIncome, totalExpense and collection of recent transactions
     * all categorized by date range
     */
    public SummaryResponse findSummary(SummaryFilter filterBy){
        //current date
        LocalDate currentDate = LocalDate.now();

        List<TransactionResponse> recentTransactionList = transactionRepository.findTop5ByOrderByDateUpdatedDesc().stream()
                .map(transactionResponseMapper::toDto).toList();

        if (SummaryFilter.TODAY.equals(filterBy)){
            DateRange dateRange = getDayRange(currentDate);
            SummaryCalculation summaryCalculation = getSummaryCalculations(dateRange);

            return new SummaryResponse(summaryCalculation, recentTransactionList);
        } else if (SummaryFilter.WEEK.equals(filterBy)) {
            DateRange dateRange = getWeekRange(currentDate);
            SummaryCalculation summaryCalculation = getSummaryCalculations(dateRange);

            return new SummaryResponse(summaryCalculation, recentTransactionList);
        } else {
            DateRange dateRange = getMonthRange(currentDate);
            SummaryCalculation summaryCalculation = getSummaryCalculations(dateRange);

            return new SummaryResponse(summaryCalculation, recentTransactionList);
        }

    }

    /* ================= Return Summary Calculations ================= */

    /**
     *
     * @param dateRange
     * @return SummaryCalculation record which holds all calculations based on the date range
     */
    public SummaryCalculation getSummaryCalculations(DateRange dateRange){
        BigDecimal totalIncome = transactionRepository.calculateTotal(TransactionType.INCOME, dateRange.startDate(),
                dateRange.endDate());
        BigDecimal totalExpenses = transactionRepository.calculateTotal(TransactionType.EXPENSE, dateRange.startDate(),
                dateRange.endDate());
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        return new SummaryCalculation(totalIncome, totalExpenses, balance);
    }

    /* ================= Generate Transaction number ================= */
    public String generateTransactionNumber(){
        String prefix = "TXN";
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString().replace("-", "").substring(0, 8).toUpperCase();
        return prefix + "-" + uniqueId;
    }

    /* ================= Return Date ranges ================= */
    public DateRange getDayRange(LocalDate date){
        return new DateRange(date, date);
    }

    public DateRange getWeekRange(LocalDate date){
        LocalDate startDate = date.with(DayOfWeek.MONDAY);
        LocalDate endDate = date.with(DayOfWeek.SUNDAY);

        return new DateRange(startDate, endDate);
    }

    public DateRange getMonthRange(LocalDate date){
        LocalDate startDate = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = date.with(TemporalAdjusters.lastDayOfMonth());

        return new DateRange(startDate, endDate);
    }
}
