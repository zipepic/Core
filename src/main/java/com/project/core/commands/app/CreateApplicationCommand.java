package com.project.core.commands.app;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
public final class CreateApplicationCommand {
  @TargetAggregateIdentifier
  private String clientId;
  private String secret;
}
