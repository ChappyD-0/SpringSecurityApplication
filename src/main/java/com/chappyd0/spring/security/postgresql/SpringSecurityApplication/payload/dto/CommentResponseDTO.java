/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/16/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto;

import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Comment;

public class CommentResponseDTO {
    public Long idComment;
    public String content;
    public String username;
    public Long tweetId;

    public CommentResponseDTO(Comment comment) {
        this.idComment = comment.getIdComment();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.tweetId = comment.getTweet().getId();
    }
}