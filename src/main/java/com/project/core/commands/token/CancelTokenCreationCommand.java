package com.project.core.commands.token;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CancelTokenCreationCommand {
    @TargetAggregateIdentifier
    private String userId;

}
