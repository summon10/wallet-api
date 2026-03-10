package com.example.walletapi.service;

import com.example.walletapi.dto.WalletOperationRequestDTO;
import com.example.walletapi.dto.WalletResponseDTO;
import com.example.walletapi.entity.OperationType;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.repository.WalletRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public WalletResponseDTO processOperation(@Valid WalletOperationRequestDTO request) {

        Wallet wallet = walletRepo.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("This wallet does not exists"));

        if (request.getOperationType() == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        } else {
            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Non-sufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        }
        Wallet saved = walletRepo.save(wallet);

        return new WalletResponseDTO(saved.getId(), saved.getBalance());
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDTO getBalance(UUID walletId) {
        Wallet wallet = walletRepo.findById(walletId)
                .orElseThrow(() -> new RuntimeException("This wallet does not exists"));
        return new WalletResponseDTO(wallet.getId(),wallet.getBalance());
    }
}
