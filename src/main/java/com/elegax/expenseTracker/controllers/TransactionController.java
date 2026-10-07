package com.elegax.expenseTracker.controllers;


import com.elegax.expenseTracker.dto.SummaryResponse;
import com.elegax.expenseTracker.dto.TransactionRequest;
import com.elegax.expenseTracker.dto.TransactionResponse;
import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.SummaryFilter;
import com.elegax.expenseTracker.entity.TransactionType;
import com.elegax.expenseTracker.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(@RequestParam(required = false)Category category,
                                                        @RequestParam(required = false)TransactionType transactionType){
        return transactionService.findAll(category, transactionType);
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponse getTransactionByTransactionId(@PathVariable String transactionId){
        return transactionService.findByTransactionId(transactionId);
    }

    @GetMapping("/summary")
    public SummaryResponse getSummary(@RequestParam SummaryFilter filterBy){
        return transactionService.findSummary(filterBy);
    }


    @PostMapping("/transactions/new")
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest){
        // Call the service to create the transaction
        transactionService.createTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
