package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenInfoEvent {
  private String userId;
  private String tokenId;
  private String refreshToken;
}