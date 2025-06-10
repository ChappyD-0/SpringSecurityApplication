/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/3/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models;

import jakarta.persistence.*;

@Entity
@Table( name = "tweet_reactions")
public class TweetReaction {

    @EmbeddedId
    LikeKey id;

    public LikeKey getId() {
        return id;
    }

    public void setId(LikeKey id) {
        this.id = id;
    }

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @MapsId("tweetId")
    @JoinColumn(name = "tweet_id")
    Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    @ManyToOne
    @MapsId("reactionId")
    @JoinColumn(name = "reaction_id")
    Reaction reaction;

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

}


