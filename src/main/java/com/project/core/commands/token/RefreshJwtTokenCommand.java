package com.project.core.commands.token;

import com.project.core.dto.TokenId;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
@Data
@Builder
public class RefreshJwtTokenCommand {
    @TargetAggregateIdentifier
    private TokenId tokenFromUserId;
    private String refreshToken;
}
