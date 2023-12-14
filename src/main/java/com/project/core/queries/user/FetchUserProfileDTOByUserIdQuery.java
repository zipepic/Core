package com.project.core.queries.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchUserProfileDTOByUserIdQuery {
    private String userId;
}
