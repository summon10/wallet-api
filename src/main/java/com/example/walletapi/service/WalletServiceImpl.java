package com.example.walletapi.service;

import com.example.walletapi.dto.WalletOperationRequestDTO;
import com.example.walletapi.dto.WalletResponseDTO;
import com.example.walletapi.entity.OperationType;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.exception.InsufficientFundsException;
import com.example.walletapi.exception.ResourceNotFoundException;
import com.example.walletapi.repository.WalletRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public WalletResponseDTO processOperation(WalletOperationRequestDTO request) {

        Wallet wallet = walletRepo.findById(request.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("This wallet does not exists"));


        if (request.getOperationType() == OperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        } else {
            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        }
        Wallet saved = walletRepo.save(wallet);

        return new WalletResponseDTO(saved.getId(), saved.getBalance(), "Operation success");
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDTO getBalance(UUID walletId) {
        Wallet wallet = walletRepo.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("This wallet does not exists"));
        return new WalletResponseDTO(wallet.getId(),wallet.getBalance(),"Operation success");
    }
}
