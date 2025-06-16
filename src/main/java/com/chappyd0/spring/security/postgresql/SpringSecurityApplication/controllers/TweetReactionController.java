/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/13/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.controllers;

import java.util.Optional;

import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Reaction;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Tweet;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.TweetReaction;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.User;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.request.TweetReactionRequest;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.ReactionRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.TweetReactionRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.TweetRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reactions")

public class TweetReactionController {

    @Autowired
    private TweetReactionRepository tweetReactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private ReactionRepository reactionRepository;


    @GetMapping("/all")
    public Page<TweetReaction> getTweet(Pageable pageable) {
        return tweetReactionRepository.findAll(pageable);
    }

    @PostMapping("/create")
    public TweetReaction createOrUpdateReaction(@Valid @RequestBody TweetReactionRequest tweetReaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User user = getValidUser(userId);
        Tweet tweet = getValidTweet(tweetReaction.getTweetId());
        Reaction reaction = getValidReaction(tweetReaction.getReactionId());

        // Buscar si ya existe una reacción de este usuario para este tweet
        Optional<TweetReaction> existing = tweetReactionRepository.findByUserIdAndTweetId(user.getId(), tweet.getId());
        TweetReaction myTweetReaction;
        if (existing.isPresent()) {
            // Si existe, actualizar la reacción
            myTweetReaction = existing.get();
            myTweetReaction.setReaction(reaction);
        } else {
            // Si no existe, crear una nueva
            myTweetReaction = new TweetReaction();
            myTweetReaction.setUser(user);
            myTweetReaction.setTweet(tweet);
            myTweetReaction.setReaction(reaction);
        }
        tweetReactionRepository.save(myTweetReaction);
        return myTweetReaction;
    }

    private User getValidUser(String userId) {
        Optional<User> userOpt = userRepository.findByUsername(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }

    private Tweet getValidTweet(Long tweetId) {
        Optional<Tweet> tweetOpt = tweetRepository.findById(tweetId);
        if (tweetOpt.isEmpty()) {
            throw new RuntimeException("Tweet not found");
        }
        return tweetOpt.get();
    }

    private Reaction getValidReaction(Long reactionId) {
        Optional<Reaction> reactionOpt = reactionRepository.findById(reactionId);
        if (reactionOpt.isEmpty()) {
            throw new RuntimeException("Reaction not found");
        }
        return reactionOpt.get();
    }

}