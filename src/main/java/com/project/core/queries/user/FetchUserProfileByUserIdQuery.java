package com.project.core.queries.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class FetchUserProfileByUserIdQuery {
  private String userId;
}
