package com.revcast.account.controller;

import com.revcast.account.dto.AccountRequest;
import com.revcast.account.dto.AccountResponse;
import com.revcast.account.service.AccountService;
import com.revcast.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "Endpoints for managing customer accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new customer account")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("Received request to create account: {}", request.getAccountName());
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Account created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieves a single account by its unique identifier")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountById(@PathVariable Long id) {
        log.info("Received request to get account with ID: {}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Account fetched successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all customer accounts")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        log.info("Received request to get all accounts");
        List<AccountResponse> response = accountService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.success(response, "Accounts fetched successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing account", description = "Updates the details of an existing account by ID")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequest request) {
        log.info("Received request to update account with ID: {}", id);
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Account updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account", description = "Deletes a customer account by its unique identifier")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        log.info("Received request to delete account with ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account deleted successfully"));
    }
}
