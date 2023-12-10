package com.project.core.events.token;

import com.project.core.dto.TokenId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenCanceledEvent {
    private TokenId tokenFromUserId;
    private String userId;
}
