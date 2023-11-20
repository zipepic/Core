package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public final class UserProfileCreatedEvent {
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private String role;
  private Date createdAt;
}
