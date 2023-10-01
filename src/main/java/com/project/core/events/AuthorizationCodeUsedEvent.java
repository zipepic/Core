package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorizationCodeUsedEvent {
  private String code;
  private String status;
}
