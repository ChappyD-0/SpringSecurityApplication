package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long idComment;
    private String content;
    private String username;
}

