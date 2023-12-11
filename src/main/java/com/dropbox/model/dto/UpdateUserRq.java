package com.dropbox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateUserRq {
    private String password;

    private String firstname;

    private String lastname;

    private LocalDate birthday;

    private LocalDateTime update;
}
