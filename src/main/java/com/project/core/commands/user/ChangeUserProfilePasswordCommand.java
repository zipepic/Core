package com.project.core.commands.user;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public final class ChangeUserProfilePasswordCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String oldPassword;
  private String newPassword;
}
