package com.app.mifi.repository;

import com.app.mifi.persist.entity.Creation;
import com.app.mifi.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreationRepository extends JpaRepository<Creation, Long> {

    List<Creation> findAllByUser_UserIdOrderByCreationIdDesc(Long userId);

    int countByUser_UserId(Long userId);

    Creation findByCreationIdAndUser_UserId(Long creationId, Long userId);

    Integer deleteAllByUser(User user);
}