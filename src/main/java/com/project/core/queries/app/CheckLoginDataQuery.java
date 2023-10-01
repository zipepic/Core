package com.project.core.queries.app;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckLoginDataQuery {
  private String clientId;
  private String secret;
}
