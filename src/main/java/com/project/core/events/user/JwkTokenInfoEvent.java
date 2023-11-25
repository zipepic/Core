package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class JwkTokenInfoEvent {
  private String userId;
  private String kid;
  private String publicKey;
  private String lastTokenId;
}
