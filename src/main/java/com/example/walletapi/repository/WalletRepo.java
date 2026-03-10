package com.example.walletapi.repository;

import com.example.walletapi.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepo extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findById(@Param("id") UUID id);

}
