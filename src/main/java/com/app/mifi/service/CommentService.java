package com.app.mifi.service;

import com.app.mifi.controller.model.CommentRequest;
import com.app.mifi.controller.model.CommentResponse;
import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest request);

    void deleteComment(Long commentId, Long userId);

    List<CommentResponse> getAllComments(Long creationId);
}
