package com.app.mifi.service.impl;

import com.app.mifi.controller.model.CommentRequest;
import com.app.mifi.controller.model.CommentResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.persist.entity.Comment;
import com.app.mifi.repository.CommentRepository;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.CommentService;
import com.app.mifi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse createComment(CommentRequest request) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(request,comment);
        comment.setByUser(userRepository.findById(request.getByUserId()).orElse(null));
        comment.setCommentedAt(LocalDate.now());
        return commentRepository.save(comment).toDto();
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment=commentRepository.findById(commentId).orElse(null);
        if(nonNull(comment)) {
            if(comment.getByUser().getUserId().equals(userId)) {
                commentRepository.deleteById(commentId);
            }
            else{
                throw new MiFiException("Unauthorized","User not Authorized to delete", HttpStatus.BAD_REQUEST);
            }
        }
    }


    @Override
    public List<CommentResponse> getAllComments(Long creationId) {
        List<CommentResponse> commentResponses= new ArrayList<>();
        commentRepository.findAllByCreationIdOrderByCommentIdDesc(creationId).forEach(comment -> {
            commentResponses.add(comment.toDto());
        });
        return commentResponses;
    }

}
