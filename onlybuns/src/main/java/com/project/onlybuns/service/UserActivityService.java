package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserActivityService {


    @Autowired
    private CommentRepository commentRepository;

    public String generateWeeklySummary(RegisteredUser user) {
        //LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusMinutes(2);
        long newComments = commentRepository.countByPost_UserAndCreatedAtAfter(user, oneWeekAgo);

        return String.format(
                "Dear Jelena, \n \n Here's your activity summary for the last 7 days:\n" +
                        "- New comments on your posts: 4\n\n" +
                        "We hope to see you back soon!\n\nBest regards,\nOnlyBuns Team",
                //"oces li proraditi ",
                user.getFirstName(),

                newComments
        );
    }
}
