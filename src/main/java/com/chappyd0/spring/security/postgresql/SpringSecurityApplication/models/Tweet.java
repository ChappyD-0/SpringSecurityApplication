/*
 * -----------------------------------
 *  Project: SpringSecurityApplication
 *  Author: chappyd-0
 *  Date: 5/27/25
 * -----------------------------------
 */
package com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "tweets")
@Getter
@Setter
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 140)
    private String tweet;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", referencedColumnName = "id")
    private User postedBy;

    public Tweet() {
    }
    public  Tweet (String tweet, User postedBy) {
        this.tweet = tweet;
        this.postedBy = postedBy;
    }



}