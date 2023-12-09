package com.project.core.events.token;

import lombok.Builder;
import lombok.Data;
import tokenlib.util.jwk.AuthProvider;

@Data
@Builder
public class TokenCreatedEvent {
    private String userId;
    private String providerId;
    private AuthProvider authProvider;
    private String role;
    private String status;
}
