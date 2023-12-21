package com.dropbox.model.entity;

import com.dropbox.model.entity.support.Audit;
import com.dropbox.model.entity.support.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private Audit audit;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<File> files = new ArrayList<>();

}
