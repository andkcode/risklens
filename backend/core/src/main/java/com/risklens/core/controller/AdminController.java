package com.risklens.core.controller;

import com.risklens.core.dto.UserResponseDto;
import com.risklens.core.service.UserDetailService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final UserDetailService userDetailService;

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<UserResponseDto>> getAllUsers(
      Principal principal, @ParameterObject Pageable pageable) {
    Page<UserResponseDto> response = userDetailService.getAllUsers(pageable);
    return ResponseEntity.ok(response);
  }
}
