package com.project.core.events.app;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationCreatedEvent {
  private String clientId;
  private String secret;
}
