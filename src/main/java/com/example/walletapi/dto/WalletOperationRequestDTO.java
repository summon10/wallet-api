package com.example.walletapi.dto;

import com.example.walletapi.entity.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class WalletOperationRequestDTO {

    @NotNull(message = "walletId must not be null")
    private UUID walletId;

    @NotNull(message = "operationType must not be null")
    private OperationType operationType;

    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;
}
