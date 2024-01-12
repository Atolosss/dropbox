package com.dropbox.repository;

import com.dropbox.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.files WHERE u.telegramUserId = :telegramUserId")
    User findUserByTelegramUserId(@Param("telegramUserId")Long telegramUserId);
}
