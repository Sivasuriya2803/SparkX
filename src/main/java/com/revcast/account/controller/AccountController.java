package com.revcast.account.controller;

import com.revcast.account.dto.AccountCreateRequest;
import com.revcast.account.dto.AccountResponse;
import com.revcast.account.entity.Account;
import com.revcast.account.service.AccountService;
import com.revcast.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Account Controller
 */
@Slf4j
@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "Account endpoints")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create account")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody AccountCreateRequest request) {
        Account account = Account.builder()
                .accountName(request.getAccountName())
                .accountCode(request.getAccountCode())
                .description(request.getDescription())
                .build();
        Account saved = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(mapToResponse(saved), "Account created"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get account")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(mapToResponse(account)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Get all accounts")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllActiveAccounts()
                .stream().map(this::mapToResponse).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .accountCode(account.getAccountCode())
                .description(account.getDescription())
                .isActive(account.getIsActive())
                .build();
    }
}

