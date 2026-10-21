package com.risklens.core.controller;

import com.risklens.core.dto.PortfolioCreateRequestDto;
import com.risklens.core.dto.PortfolioResponseDto;
import com.risklens.core.service.PortfolioService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<PortfolioResponseDto> createPortfolio(
        @Valid @RequestBody PortfolioCreateRequestDto createRequestDto,
        Principal principal) {
        String userEmail = principal.getName();
        PortfolioResponseDto response = portfolioService.createPortfolio(userEmail, createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PortfolioResponseDto>> getPortfolios(
        Principal principal,
        @ParameterObject Pageable pageable) {
        Page<PortfolioResponseDto> response = portfolioService.getPortfolios(principal.getName(), pageable);
        return ResponseEntity.ok(response);
    }
}
