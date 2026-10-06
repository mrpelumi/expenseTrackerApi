package com.elegax.expenseTracker.services;

import com.elegax.expenseTracker.dto.TransactionResponse;
import com.elegax.expenseTracker.mappers.TransactionRequestMapper;
import com.elegax.expenseTracker.mappers.TransactionResponseMapper;
import com.elegax.expenseTracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionResponseMapper transactionResponseMapper;
    private final TransactionRequestMapper transactionRequestMapper;

    public List<TransactionResponse> findAll(){
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "dateUpdated"))
                .stream().map(transactionResponseMapper::toDto)
                .toList();
    }

    public TransactionResponse findById(Long id){
        return transactionRepository.findById(id)
                .map(transactionResponseMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }
}
