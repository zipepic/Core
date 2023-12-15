package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;
import tokenlib.util.jwk.AuthProvider;

@Data
@Builder
public class UserProfileProviderMappingLookUpCreatedEvent {
    private String userId;
    private String providerId;
    private AuthProvider authProvider;
}
