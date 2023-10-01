package com.project.core.commands.app;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
//TODO refactoring this class(delete unused code) and sort classes by package(application, user)
public class CreateApplicationCommand {
  @TargetAggregateIdentifier
  private String clientId;
  private String secret;
}
