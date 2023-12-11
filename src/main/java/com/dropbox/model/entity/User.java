package com.dropbox.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Document
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String mail;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDate birthday;
    private LocalDateTime create;
    private LocalDateTime update;

    public User(String mail, String password, LocalDate birthday, LocalDateTime create) {
        this.mail = mail;
        this.password = password;
        this.birthday = birthday;
        this.create = create;
    }
}
