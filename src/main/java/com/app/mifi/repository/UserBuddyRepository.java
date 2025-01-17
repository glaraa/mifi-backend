package com.app.mifi.repository;

import com.app.mifi.persist.entity.UserBuddy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBuddyRepository extends JpaRepository<UserBuddy, Integer> {
    List<UserBuddy> findAllByUser_UserId(Long userId);

    Boolean existsByUser_UserIdAndBuddyUser_UserId(Long userId, Long viewUserId);

    UserBuddy findByUser_UserIdAndBuddyUser_UserId(Long userId, Long buddyUserId);
}
