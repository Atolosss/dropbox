package com.dropbox.repository;

import com.dropbox.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByTelegramUserId(Long id);
}
