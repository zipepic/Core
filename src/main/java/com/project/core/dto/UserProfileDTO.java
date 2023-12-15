package com.project.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class UserProfileDTO {
    private String userId;
    private String userName;
    private String userStatus;
    private String role;
    private String tokenId;
    private String githubId;
    private String googleId;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Date deleteAt;
}
