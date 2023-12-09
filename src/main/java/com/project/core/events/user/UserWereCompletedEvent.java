package com.project.core.events.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWereCompletedEvent {
    private String userId;
    private String status;
}
