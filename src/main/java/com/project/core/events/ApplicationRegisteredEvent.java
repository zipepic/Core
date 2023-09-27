package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationRegisteredEvent {
  private String clientId;
  private String code;
}
