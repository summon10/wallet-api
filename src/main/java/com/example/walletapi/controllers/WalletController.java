package com.example.walletapi.controllers;


import com.example.walletapi.dto.WalletOperationRequestDTO;
import com.example.walletapi.dto.WalletResponseDTO;
import com.example.walletapi.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponseDTO> processOperation(
            @Valid @RequestBody WalletOperationRequestDTO request) {
        WalletResponseDTO response = walletService.processOperation(request);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<WalletResponseDTO> getBalance (@PathVariable UUID walletId)
    {
        WalletResponseDTO response = walletService.getBalance(walletId);
        return ResponseEntity.ok(response);
    }

}
