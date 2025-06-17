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
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.CommentRepository;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.repository.TweetReactionRepository;
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
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto.TweetDetailsDTO;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.Comment;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.models.TweetReaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto.CommentDTO;
import com.chappyd0.spring.security.postgresql.SpringSecurityApplication.payload.dto.TweetReactionDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TweetReactionRepository tweetReactionRepository;

    private final Path uploadDir = Paths.get("uploads");


    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("/api/tweets/image/" + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload image: " + e.getMessage());
        }
    }


    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public Page<TweetResponse> getTweet(Pageable pageable) {
        Page<Tweet> tweets = tweetRepository.findAll(pageable);
        return new PageImpl<>(
            tweets.getContent().stream()
                .map(tweet -> new TweetResponse(
                    tweet.getId(),
                    tweet.getTweet(),
                    tweet.getPostedBy() != null ? tweet.getPostedBy().getUsername() : null,
                    tweet.getImageUrl() // <-- Añade imageUrl en la respuesta
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
        User user = getValidUser(userId);
        Tweet myTweet = new Tweet(tweet.getTweet(), user);
        myTweet.setPostedBy(user);
        myTweet.setImageUrl(tweet.getImageUrl());
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

    @GetMapping("/details/{id}")
    public ResponseEntity<TweetDetailsDTO> getTweetDetails(@PathVariable("id") Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        List<CommentDTO> comments = commentRepository.findByTweetId(id)
                .stream()
                .map(c -> new CommentDTO(c.getIdComment(), c.getContent(), c.getUser().getUsername()))
                .toList();
        List<TweetReactionDTO> reactions = tweetReactionRepository.findAll()
                .stream()
                .filter(r -> r.getTweetId().equals(id))
                .map(r -> new TweetReactionDTO(r.getId(), r.getReactionId(), r.getUser() != null ? r.getUser().getUsername() : null))
                .toList();
        TweetDetailsDTO dto = new TweetDetailsDTO(
                tweet.getId(),
                tweet.getTweet(),
                tweet.getImageUrl(),
                comments,
                reactions
        );
        return ResponseEntity.ok(dto);
    }

}
