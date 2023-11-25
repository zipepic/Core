package com.project.core.events.code;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public final class AuthorizationCodeGeneratedEvent {
  private String code;
  private String userId;
  private String clientId;
  private Date expiresAt;
  private String scope;
  private String status;
  private String sessionId;
}
