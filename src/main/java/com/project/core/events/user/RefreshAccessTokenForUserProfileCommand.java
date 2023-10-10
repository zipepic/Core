package com.project.core.events.user;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RefreshAccessTokenForUserProfileCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String refreshToken;
  private Claims claims;
}
