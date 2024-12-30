package com.app.mifi.repository;

import com.app.mifi.persist.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
