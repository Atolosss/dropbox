package com.dropbox.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(name = "create_date_time", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
    @Builder.Default
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<File> files = new ArrayList<>();

}
