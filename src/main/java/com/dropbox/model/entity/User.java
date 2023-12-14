package com.dropbox.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    @Builder.Default
    private List<UserFile> files = new ArrayList<>();

}
