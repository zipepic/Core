package com.project.core.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
public class CreateApplicationCommand {
  @TargetAggregateIdentifier
  private String clientId;
  private String secret;
}