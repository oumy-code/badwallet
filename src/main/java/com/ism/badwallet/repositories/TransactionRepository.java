package com.ism.badwallet.repositories;

import com.ism.badwallet.entities.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Long> {
    Page<TransactionHistory> findByWalletPhone(String walletPhone, Pageable pageable);
}