package com.project.core.commands.user;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import tokenlib.util.jwk.AuthProvider;

@Data
@Builder
public class CreateUserFromProviderIdCommand {
    @TargetAggregateIdentifier
    private String userId;
    private String userName;
    private String providerId;
    private AuthProvider authProvider;
}
