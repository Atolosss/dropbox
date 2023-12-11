package com.dropbox.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    @BsonId
    private String id;

    private String mail;

    private String password;

    private LocalDate birthday;

    @Override
    public String toString() {
        return "User{" +
                "mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
