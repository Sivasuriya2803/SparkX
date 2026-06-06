package com.revcast.account.repository;

import com.revcast.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Account Repository
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountCode(String accountCode);

    Optional<Account> findByAccountName(String accountName);

    List<Account> findByIsActiveTrue();

    Boolean existsByAccountCode(String accountCode);

    Boolean existsByAccountName(String accountName);
}

