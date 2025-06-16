package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TweetReactionDTO {
    private Long id;
    private Long reactionId;
    private String username;
}

