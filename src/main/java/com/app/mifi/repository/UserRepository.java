package com.app.mifi.repository;

import com.app.mifi.persist.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);

    List<User> findAllByCategoryAndUserIdNot(String category, Long userId);

    List<User> findAllByUserIdNot(Long userId);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
