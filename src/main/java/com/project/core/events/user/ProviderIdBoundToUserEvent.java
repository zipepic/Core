package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderIdBoundToUserEvent {
    private String userId;
    private String providerId;
    private String providerType;
}
