package com.project.core.commands.user;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public final class RefreshAccessTokenForUserProfileCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String refreshToken;
}
