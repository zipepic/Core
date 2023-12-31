package com.project.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenAuthorizationCodeDTO implements TokenDTO {
  private String accessToken;
  private Integer expiresIn;
  private Integer refreshExpiresIn;
  private String refreshToken;
  private String tokenType;
  private String tokenId;
}
