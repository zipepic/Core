package com.project.core.commands.user;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
public class BindProviderIdToUserCommand {
    @TargetAggregateIdentifier
    private String userId;
    private String providerId;
    private String providerType;


}
