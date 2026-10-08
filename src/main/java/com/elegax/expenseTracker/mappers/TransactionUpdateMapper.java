package com.elegax.expenseTracker.mappers;

import com.elegax.expenseTracker.dto.TransactionUpdateRequest;
import com.elegax.expenseTracker.entity.Transaction;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionUpdateMapper {
    Transaction toEntity(TransactionUpdateRequest transactionUpdateRequest);

    TransactionUpdateRequest toDto(Transaction transaction);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction partialUpdate(TransactionUpdateRequest transactionUpdateRequest, @MappingTarget Transaction transaction);
}