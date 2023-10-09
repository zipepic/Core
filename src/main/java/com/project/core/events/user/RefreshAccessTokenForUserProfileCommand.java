package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RefreshAccessTokenForUserProfileCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String refreshToken;
}
