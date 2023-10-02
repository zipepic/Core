package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenGeneratedEvent {
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  private String accessToken;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;
}
