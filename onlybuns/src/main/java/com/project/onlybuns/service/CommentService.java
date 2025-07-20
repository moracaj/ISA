package com.project.onlybuns.service;

import com.project.onlybuns.model.Comment;
import com.project.onlybuns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepo;

    @Autowired
    public CommentService(CommentRepository commentRepo) {
        this.commentRepo = commentRepo;
    }

    public List<Comment> fetchAllComments() {
        return commentRepo.findAll();
    }

    public Optional<Comment> findCommentById(Long id) {
        return commentRepo.findById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepo.deleteById(id);
    }
}
