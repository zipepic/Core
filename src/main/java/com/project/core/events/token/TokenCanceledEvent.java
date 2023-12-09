package com.project.core.events.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenCanceledEvent {
    private String userId;
}
