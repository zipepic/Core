package com.project.core.events;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class UserProfileCreatedEvent {
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private Date createdAt;
}
