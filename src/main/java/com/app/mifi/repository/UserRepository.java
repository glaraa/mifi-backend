package com.app.mifi.repository;

import com.app.mifi.persist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    List<User> findAllByCategoryAndUserIdNot(String category, Long userId);

    List<User> findAllByUserIdNot(Long userId);
}
