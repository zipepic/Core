package com.project.core.commands.code;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public final class UseAuthorizationCodeCommand {
  @TargetAggregateIdentifier
  private String code;

}
