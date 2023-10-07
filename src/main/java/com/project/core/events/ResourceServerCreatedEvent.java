package com.project.core.events;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ResourceServerCreatedEvent {
  private String resourceServerName;
  private String secret;
  private Date createAt;
  private String status;
}
