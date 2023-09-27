package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationLoggedInEvent {
  private String clientId;
}
