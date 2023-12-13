package com.dropbox.model.entity;

import com.dropbox.model.enums.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document
public class UserFile {
    @Id
    private String id;
    private String name;
    private FileType fileType;
    private String url;
    private User user;

}
