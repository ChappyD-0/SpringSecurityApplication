/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 5/27/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.controllers;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Tweet;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.User;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Collectors;

import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.response.TweetResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @GetMapping("/all")
    public Page<TweetResponse> getTweet(Pageable pageable) {
        Page<Tweet> tweets = tweetRepository.findAll(pageable);
        return new PageImpl<>(
            tweets.getContent().stream()
                .map(tweet -> new TweetResponse(
                    tweet.getId(),
                    tweet.getTweet(),
                    tweet.getPostedBy() != null ? tweet.getPostedBy().getUsername() : null
                ))
                .collect(Collectors.toList()),
            pageable,
            tweets.getTotalElements()
        );
    }

    /**
     * Creates a new tweet.
     *
     * @param tweet the tweet to create
     * @return the created tweet
     */
    @PostMapping("/create")
    public Tweet createTweet(@Valid @RequestBody Tweet tweet) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("userid : " + userId  );


        User user = getValidUser(userId);
        System.out.println("user");

        System.out.println(user);
        Tweet myTweet = new Tweet(tweet.getTweet(),user);
        myTweet.setPostedBy(user);
        tweetRepository.save(myTweet);

        return myTweet;
    }
    @Autowired
    private UserRepository userRepository;


    private User getValidUser(String userId) {
        Optional<User> userOpt = userRepository.findByUsername(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get();
    }



}
