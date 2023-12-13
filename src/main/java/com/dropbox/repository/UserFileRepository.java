package com.dropbox.repository;

import com.dropbox.model.entity.UserFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserFileRepository extends MongoRepository<UserFile, String> {
}
