package com.app.mifi.controller;

import com.app.mifi.controller.model.CommentRequest;
import com.app.mifi.controller.model.CommentResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static com.app.mifi.constant.Constant.REQUEST_BY;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<MiFiResponse<CommentResponse>> createComment(@RequestHeader(name = REQUEST_BY) String requestBy, @RequestBody CommentRequest commentRequest) {
        log.info("Add comment requested by user [{}], requestedBy [{}]",commentRequest.getByUserId(),requestBy);
        CommentResponse response = commentService.createComment(commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(MiFiResponse.<CommentResponse>builder().response(response).build());
    }

    @DeleteMapping("/{userId}/comment/{commentId}")
    public ResponseEntity<MiFiResponse<String>> deleteComment(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                              @PathVariable Long commentId, @PathVariable Long userId) {
        log.info("Delete comment requested by user [{}], requestedBy [{}]",userId,requestBy);
        commentService.deleteComment(commentId,userId);
        return ResponseEntity.ok(MiFiResponse.<String>builder().response("Comment deleted successfully").build());
    }

    @GetMapping("/{creationId}/comment")
    public ResponseEntity<MiFiResponse<List<CommentResponse>>> getAllCommentsForCreation(@RequestHeader(name = REQUEST_BY) String requestBy,
                                                                              @PathVariable Long creationId) {
        log.info("View all comments for creation [{}], requestedBy [{}]",creationId,requestBy);
        List<CommentResponse> responses = commentService.getAllComments(creationId);
        return ResponseEntity.ok(MiFiResponse.<List<CommentResponse>>builder().response(responses).build());
    }
}
