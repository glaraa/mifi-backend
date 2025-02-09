package com.app.mifi.repository;

import com.app.mifi.persist.entity.Comment;
import com.app.mifi.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCreationIdOrderByCommentIdDesc(Long creationId);

    Integer deleteAllByByUser(User user);
}
