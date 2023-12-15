package com.project.core.events.token;

import com.project.core.dto.TokenId;
import lombok.Builder;
import lombok.Data;
import tokenlib.util.jwk.AuthProvider;

@Data
@Builder
public class TokenCreatedEvent {
    private TokenId tokenFromUserId;
    private String providerId;
    private AuthProvider authProvider;
    private String role;
    private String status;
    private String userId;
}
