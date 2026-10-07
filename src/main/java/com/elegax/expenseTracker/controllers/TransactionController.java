package com.elegax.expenseTracker.controllers;


import com.elegax.expenseTracker.dto.SummaryResponse;
import com.elegax.expenseTracker.dto.TransactionRequest;
import com.elegax.expenseTracker.dto.TransactionResponse;
import com.elegax.expenseTracker.entity.Category;
import com.elegax.expenseTracker.entity.SummaryFilter;
import com.elegax.expenseTracker.entity.TransactionType;
import com.elegax.expenseTracker.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Transactions", description = "Endpoints for managing transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(operationId = "getTransaction",
            summary = "Get Transactions",
            description = """
                    Return all transactions.
                    Return transactions filtered by category and transaction type.    
                    """)
    @ApiResponses({
            @ApiResponse(description = "Transaction retrieved successfully",
                    responseCode = "200")
    })
    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(
            @Parameter(description = "filter by category", example = "FOOD")
            @RequestParam(required = false)Category category,

            @Parameter(description = "filter by transaction type", example = "INCOME")
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

    @Operation(
            summary = "Delete Transaction",
            description = "Delete transaction by the transaction id"
    )
    @ApiResponses({
            @ApiResponse(description = "Transaction successfully deleted", responseCode = "204"),
            @ApiResponse(description = "Transaction not found", responseCode = "404")
    })
    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String transactionId){
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
}
