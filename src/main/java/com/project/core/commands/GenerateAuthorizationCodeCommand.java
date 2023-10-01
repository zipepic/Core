package com.project.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

@Data
@Builder
public class GenerateAuthorizationCodeCommand {
  @TargetAggregateIdentifier
  private String code;
  private String userId;
  private String clientId;
  private String scope;
  private String sessionId;
}
