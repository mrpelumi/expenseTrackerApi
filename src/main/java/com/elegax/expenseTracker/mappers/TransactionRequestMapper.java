package com.elegax.expenseTracker.mappers;

import com.elegax.expenseTracker.dto.TransactionRequest;
import com.elegax.expenseTracker.entity.Transaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionRequestMapper {
    Transaction toEntity(TransactionRequest transactionRequest);

    TransactionRequest toDto(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction partialUpdate(TransactionRequest transactionRequest, @MappingTarget Transaction transaction);
}