package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TweetDetailsDTO {
    private Long tweetId;
    private String tweetContent;
    private String imageUrl;
    private List<CommentDTO> comments;
    private List<TweetReactionDTO> reactions;
}
