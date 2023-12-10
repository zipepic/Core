package com.project.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileProviderMappingLookUpDTO {
    private String userId;
    private String githubId;
    private String googleId;
}
