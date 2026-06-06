package com.revcast.account.service;

import com.revcast.account.dto.AccountRequest;
import com.revcast.account.dto.AccountResponse;
import com.revcast.account.entity.Account;
import com.revcast.account.repository.AccountRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        log.info("Creating new account with code: {}", request.getAccountCode());

        if (accountRepository.existsByAccountCode(request.getAccountCode())) {
            throw new ValidationException("Account with code " + request.getAccountCode() + " already exists.");
        }
        if (accountRepository.existsByAccountName(request.getAccountName())) {
            throw new ValidationException("Account with name " + request.getAccountName() + " already exists.");
        }

        Account account = Account.builder()
                .accountName(request.getAccountName())
                .accountCode(request.getAccountCode())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getId());
        return mapToAccountResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        log.info("Fetching account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return mapToAccountResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        log.info("Fetching all accounts");
        return accountRepository.findAll().stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        log.info("Updating account with ID: {}", id);
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));

        if (!existingAccount.getAccountCode().equals(request.getAccountCode()) &&
                accountRepository.existsByAccountCode(request.getAccountCode())) {
            throw new ValidationException("Account with code " + request.getAccountCode() + " already exists.");
        }
        if (!existingAccount.getAccountName().equals(request.getAccountName()) &&
                accountRepository.existsByAccountName(request.getAccountName())) {
            throw new ValidationException("Account with name " + request.getAccountName() + " already exists.");
        }

        existingAccount.setAccountName(request.getAccountName());
        existingAccount.setAccountCode(request.getAccountCode());
        existingAccount.setDescription(request.getDescription());
        existingAccount.setIsActive(request.getIsActive());

        Account updatedAccount = accountRepository.save(existingAccount);
        log.info("Account updated successfully with ID: {}", updatedAccount.getId());
        return mapToAccountResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account with ID: {}", id);
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with ID: " + id);
        }
        accountRepository.deleteById(id);
        log.info("Account deleted successfully with ID: {}", id);
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .accountCode(account.getAccountCode())
                .description(account.getDescription())
                .isActive(account.getIsActive())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
