package com.dropbox.model.entity;

import com.dropbox.model.entity.support.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "file")
public class UserFile extends BaseEntity {
    private String key;
    private String description;
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
