package com.revcast.account.service;

import com.revcast.account.entity.Account;
import com.revcast.account.repository.AccountRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Account Service
 */
@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create account
     */
    @Transactional
    public Account createAccount(Account account) {
        log.info("Creating account: {}", account.getAccountName());

        if (accountRepository.existsByAccountCode(account.getAccountCode())) {
            throw new ValidationException("Account with code " + account.getAccountCode() + " already exists");
        }

        if (accountRepository.existsByAccountName(account.getAccountName())) {
            throw new ValidationException("Account with name " + account.getAccountName() + " already exists");
        }

        account.setIsActive(true);
        return accountRepository.save(account);
    }

    /**
     * Get account by ID
     */
    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    /**
     * Get account by code
     */
    @Transactional(readOnly = true)
    public Account getAccountByCode(String code) {
        return accountRepository.findByAccountCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "code", code));
    }

    /**
     * Get all active accounts
     */
    @Transactional(readOnly = true)
    public List<Account> getAllActiveAccounts() {
        return accountRepository.findByIsActiveTrue();
    }

    /**
     * Update account
     */
    @Transactional
    public Account updateAccount(Long id, Account accountUpdate) {
        log.info("Updating account: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        account.setAccountName(accountUpdate.getAccountName());
        account.setDescription(accountUpdate.getDescription());

        return accountRepository.save(account);
    }

    /**
     * Deactivate account
     */
    @Transactional
    public Account deactivateAccount(Long id) {
        log.info("Deactivating account: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        account.setIsActive(false);
        return accountRepository.save(account);
    }
}

