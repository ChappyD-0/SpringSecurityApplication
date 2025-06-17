package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TweetResponse {
    private Long id;
    private String tweet;
    private String postedByUsername;
    private String imageUrl; // <-- Nuevo campo
}

