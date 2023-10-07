package com.project.core.commands.token;

import com.project.core.commands.ResourceServerDTO;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;

@Data
@Builder
public class GenerateTokenCommand {
  @TargetAggregateIdentifier
  private String tokenId;
  private String userId;
  private String clientId;
  private String scope;

  private List<ResourceServerDTO> resourceServerDTOList;
}
