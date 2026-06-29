package com.ism.badwallet.repositories;

import com.ism.badwallet.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPhoneNumber(String phoneNumber);
    Optional<Wallet> findByCode(String code);
}