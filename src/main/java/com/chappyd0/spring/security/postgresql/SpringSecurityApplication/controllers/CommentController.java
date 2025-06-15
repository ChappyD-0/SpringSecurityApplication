package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.controllers;

import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Comment;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Tweet;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.User;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.request.CreateCommentRequest;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.CommentRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.TweetRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        Optional<Tweet> tweet = tweetRepository.findById(request.getTweetId());
        if (tweet.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid tweet ID");
        }

        // Obtener usuario autenticado del JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTweet(tweet.get());
        comment.setUser(user.get());
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }
}

