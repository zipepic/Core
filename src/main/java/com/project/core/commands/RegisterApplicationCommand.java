package com.project.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RegisterApplicationCommand {
  @TargetAggregateIdentifier
  private String clientId;
  private String responseType;
  private String state;
  private String scope;
  private String redirectUrl;
}
