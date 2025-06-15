/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/3/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table( name = "tweet_reactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "tweet_id"}
                ),

        }
)

public class TweetReaction {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "reaction_id")
    Long reactionId;

    @Setter
    @Column(name = "user_id")
    Long userId;

    @Setter
    @Column(name = "tweet_id")
    Long tweetId;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    public void setUser(User user) {
        this.userId = user.getId();
        this.user = user;
    }

    @ManyToOne
    @MapsId("tweetId")
    @JoinColumn(name = "tweet_id")
    Tweet tweet;

    public void setTweet(Tweet tweet) {
        this.tweetId = tweet.getId();
        this.tweet = tweet;
    }

    @ManyToOne
    @MapsId("reactionId")
    @JoinColumn(name = "reaction_id")
    Reaction reaction;

    public void setReaction(Reaction reaction) {
        this.reactionId = reaction.getId();
        this.reaction = reaction;
    }

}
