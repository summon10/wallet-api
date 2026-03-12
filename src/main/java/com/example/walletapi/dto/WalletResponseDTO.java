package com.example.walletapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WalletResponseDTO {
    private UUID walletId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private BigDecimal balance = BigDecimal.valueOf(0);
    private String message= "";

    public WalletResponseDTO(String message) {
        this.message = message;
    }
}

