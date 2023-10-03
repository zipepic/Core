package com.project.core.queries;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchTokensByTokenId {
  private String tokenId;
}
