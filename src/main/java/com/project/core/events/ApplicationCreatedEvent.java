package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationCreatedEvent {
  private String clientId;
  private String secret;
}
