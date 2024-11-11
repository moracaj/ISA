package com.project.onlybuns.service;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CommentService {

    private final CommentRepository repository;

    @Autowired
    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public List<Comment> getAllComments() {
        return repository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return repository.findById(id);
    }

    public Comment createOrUpdateComment(Comment comment) {
        return repository.save(comment);
    }

    public void removeComment(Long id) {
        repository.deleteById(id);
    }
}
