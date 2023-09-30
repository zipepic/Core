package com.project.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OneTimeCodeUserProfileGeneratedEvent {
  private String userId;
  private String code;
}
