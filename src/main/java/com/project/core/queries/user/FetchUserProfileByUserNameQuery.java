package com.project.core.queries.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class FetchUserProfileByUserNameQuery {
  private String userName;
}
