package com.elegax.expenseTracker.controllers;


import com.elegax.expenseTracker.dto.TransactionResponse;
import com.elegax.expenseTracker.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/transactions/all")
    public List<TransactionResponse> getAllTransactions(){
        return transactionService.findAll();
    }

    @GetMapping("/transactions/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id){
        return transactionService.findById(id);
    }

    //@PostMapping
}
