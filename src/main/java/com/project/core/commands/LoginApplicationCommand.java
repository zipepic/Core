package com.project.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
public class LoginApplicationCommand {
  @TargetAggregateIdentifier
  private String clientId;
  private String secret;
  private String grantType;
  private String code;
  private String redirectUrl;
}
