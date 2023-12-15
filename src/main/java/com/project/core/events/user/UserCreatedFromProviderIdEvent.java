package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;
import tokenlib.util.jwk.AuthProvider;

import java.util.Date;

@Data
@Builder
public class UserCreatedFromProviderIdEvent {
    private String userId;
    private String userName;
    private String providerId;
    private AuthProvider authProvider;
    private String userStatus;
    private String role;
    private Date createdAt;
}
