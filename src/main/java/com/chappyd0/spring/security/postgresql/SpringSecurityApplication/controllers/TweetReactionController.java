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
    public TweetReaction createReaction(@Valid @RequestBody TweetReactionRequest tweetReaction) {
        System.out.println("tweetid : " + tweetReaction.getTweetId()  );
        System.out.println("reactiontid : " + tweetReaction.getReactionId()  );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("userid : " + userId  );


        User user = getValidUser(userId);
        System.out.println("user");

        System.out.println(user);

        TweetReaction myTweetReaction = new TweetReaction();
        Tweet myTweet = getValidTweet(tweetReaction.getTweetId());
        System.out.println("object tweet : " );
        System.out.println(myTweet );


        Reaction myReaction = getValidReaction(tweetReaction.getReactionId());
        System.out.println("object reaction : "   );
        System.out.println( myReaction );

        //myTweetReaction.setUserId(user.getId());
        //myTweetReaction.setTweetId(myTweet.getId());
        //myTweetReaction.setReactionId(myReaction.getId());

        myTweetReaction.setUser(user);
        myTweetReaction.setTweet(myTweet);
        myTweetReaction.setReaction(myReaction);

        System.out.println("tweet reaction : "   );
        System.out.println( myTweetReaction.getReactionId());
        System.out.println( myTweetReaction.getTweetId());

        System.out.println( myTweetReaction.getUserId());


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