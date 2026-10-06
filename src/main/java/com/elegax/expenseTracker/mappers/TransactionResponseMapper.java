package com.elegax.expenseTracker.mappers;

import com.elegax.expenseTracker.dto.TransactionResponse;
import com.elegax.expenseTracker.entity.Transaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionResponseMapper {
    Transaction toEntity(TransactionResponse transactionResponse);

    TransactionResponse toDto(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction partialUpdate(TransactionResponse transactionResponse, @MappingTarget Transaction transaction);
}