package com.app.mifi.repository;

import com.app.mifi.persist.entity.BuddyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuddyRequestRepository extends JpaRepository<BuddyRequest, Long> {
    List<BuddyRequest> findAllByRecipientUser_UserId(Long userId);

    BuddyRequest findByRequesterUser_UserIdAndRecipientUser_UserId(Long requesterUserId, Long userId);

    Boolean existsByRequesterUser_UserIdAndRecipientUser_UserId(Long userId, Long viewUserId);
}
