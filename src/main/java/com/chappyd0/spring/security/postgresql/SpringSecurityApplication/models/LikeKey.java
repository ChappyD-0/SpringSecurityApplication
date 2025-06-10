/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 6/3/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
@Embeddable
class LikeKey implements Serializable {

    @Column(name = "reaction_id")
    Long reactionId;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "tweet_id")
    Long tweetId;

    public Long getReactionId() {
        return reactionId;
    }
    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getTweetId() {
        return tweetId;
    }
    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }
}
