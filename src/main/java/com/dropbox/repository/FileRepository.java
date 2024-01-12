package com.dropbox.repository;

import com.dropbox.model.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UserFile, Long> {
}
