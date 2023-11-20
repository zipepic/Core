package com.project.core.commands.user;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
@Data
@Builder
public final class CreateUserProfileCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
}
