package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class UserProfilePasswordChangedEvent {
  private String userId;
  private String newPassword;
}
