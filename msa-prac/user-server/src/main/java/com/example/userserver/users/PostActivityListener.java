package com.example.userserver.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * PostActivityListener
 *
 * @author jaypark
 * @version 1.0.0
 * @since 8/25/24
 */
@Component
public class PostActivityListener {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    public PostActivityListener(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @KafkaListener(topics = "post.updated", groupId = "user-server")
    public void listen(String message) {
        try {
            PostActivity post = objectMapper.readValue(message, PostActivity.class);
            userService.updateLastPost(post.getUserId(), post.getLastUpdatedId(), post.getLastUploadDatetime());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
