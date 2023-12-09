package com.project.core.commands.token;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import tokenlib.util.jwk.AuthProvider;
@Data
@Builder
public class CreateTokenCommand {
    @TargetAggregateIdentifier
    private String userId;
    private String providerId;
    private AuthProvider authProvider;
    private String role;
}
