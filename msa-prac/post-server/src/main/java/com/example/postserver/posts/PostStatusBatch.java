package com.example.postserver.posts;

import com.example.postserver.posts.uploader.UploaderInfo;
import com.example.postserver.posts.uploader.UploaderService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostStatusBatch {

    private final UploaderService uploaderService;
    private final PostService postService;
    private final Logger logger = LoggerFactory.getLogger(PostStatusBatch.class);

    public PostStatusBatch(UploaderService uploaderService, PostService postService) {
        this.uploaderService = uploaderService;
        this.postService = postService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void updatePostStatus() {
        logger.info("Post check batch start");
        List<Integer> users = uploaderService.getAllUser().stream().map(UploaderInfo::getUserId).toList();
        List<Integer> deletedUsers = postService.getUploaderByStatus("hide");
        List<Integer> activeUsers = postService.getUploaderByStatus("active");

        List<Integer> toBeActiveUsers = users.stream().filter(deletedUsers::contains).toList();
        List<Integer> toBeHideUsers= activeUsers.stream().filter(userId -> !users.contains(userId)).toList();

        logger.info("Consistency Work - hide -> active : {} / active -> hide : {}", toBeActiveUsers.size(), toBeHideUsers.size());

        toBeActiveUsers.forEach(postService::activatePost);
        toBeHideUsers.forEach(postService::deactivatePost);

        logger.info("Post check batch end");

        // single source of truth - user-server
    }
}