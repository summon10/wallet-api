package com.example.walletapi.service;

import com.example.walletapi.dto.WalletOperationRequestDTO;
import com.example.walletapi.dto.WalletResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface WalletService {

    public WalletResponseDTO processOperation (@Valid WalletOperationRequestDTO wallet);
    public WalletResponseDTO getBalance(UUID walletId);

}
