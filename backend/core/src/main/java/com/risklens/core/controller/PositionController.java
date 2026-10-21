package com.risklens.core.controller;

import com.risklens.core.dto.PositionCreateRequestDto;
import com.risklens.core.dto.PositionResponseDto;
import com.risklens.core.service.PositionService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/portfolios/{portfolioId}/positions/add")
    public ResponseEntity<PositionResponseDto> addPosition(
        @PathVariable UUID portfolioId,
        @Valid @RequestBody PositionCreateRequestDto createRequestDto,
        Principal principal) {
        String userEmail = principal.getName();
        PositionResponseDto response =
            positionService.addPosition(userEmail, portfolioId, createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/portfolios/{portfolioId}/positions")
    public ResponseEntity<Page<PositionResponseDto>> getPositions(
        @PathVariable UUID portfolioId,
        Principal principal,
        @ParameterObject Pageable pageable) {
        String userEmail = principal.getName();
        Page<PositionResponseDto> response =
            positionService.getPositions(userEmail, portfolioId, pageable);
        return ResponseEntity.ok(response);
    }
}
