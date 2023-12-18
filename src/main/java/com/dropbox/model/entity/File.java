package com.dropbox.model.entity;

import com.dropbox.model.enums.FileType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString

@Entity
@Table(name = "file")
public class File {
    @Id
    private Long id;
    private String name;
    private FileType fileType;
    private String url;
    private String userId;

}
