package com.project.core.events.token;

import com.project.core.dto.TokenId;
import lombok.Builder;
import lombok.Data;
import tokenlib.util.jwk.SimpleJWK;

@Data
@Builder
public class TokenGeneratedEvent {
    private TokenId tokenId;
    private String kid;
    private String lastKid;
    private String jwk;
}
