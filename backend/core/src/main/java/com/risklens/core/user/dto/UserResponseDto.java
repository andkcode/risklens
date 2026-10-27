package com.risklens.core.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private UUID id;
  private String email;
  private String role;
  private boolean isActive;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
