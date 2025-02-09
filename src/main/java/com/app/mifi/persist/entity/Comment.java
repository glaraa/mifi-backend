package com.app.mifi.persist.entity;

import com.app.mifi.controller.model.CommentResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "by_user_id")
    private User byUser;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "creation_id")
    private Long creationId;

    @Column(name = "commented_at")
    private LocalDate commentedAt;

    public CommentResponse toDto() {
        CommentResponse commentResponse= CommentResponse.builder().build();
        BeanUtils.copyProperties(this,commentResponse);
        commentResponse.setByUser(this.byUser.toDtos());
        commentResponse.setCommentedAt(commentedAt.format(DateTimeFormatter.ISO_LOCAL_DATE));
        return commentResponse;
    }
}