package com.dropbox.repository;

import com.dropbox.model.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    List<File> findAllByUserId(String userId);
}
