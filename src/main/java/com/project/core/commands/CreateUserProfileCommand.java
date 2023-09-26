package com.project.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;
@Data
@Builder
public class CreateUserProfileCommand {
  @TargetAggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private Date createdAt;
}