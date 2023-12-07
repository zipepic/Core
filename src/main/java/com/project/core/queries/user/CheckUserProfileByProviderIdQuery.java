package com.project.core.queries.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckUserProfileByProviderIdQuery {
    private String providerId;

    private String providerType;
}
