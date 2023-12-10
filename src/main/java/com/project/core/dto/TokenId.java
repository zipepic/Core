package com.project.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenId {
    private String userId;
    private final String sol = "token";

    public TokenId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return userId + sol;
    }
}
