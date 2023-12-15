package com.project.core.commands.token;

import com.project.core.dto.TokenId;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import tokenlib.util.jwk.AuthProvider;
@Data
@Builder
public class CreateTokenCommand {
    @TargetAggregateIdentifier
    private TokenId tokenFromUserId;
    private String providerId;
    private AuthProvider authProvider;
    private String role;
}
