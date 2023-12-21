package com.dropbox.model.entity;

import com.dropbox.model.entity.support.Audit;
import com.dropbox.model.entity.support.BaseEntity;
import com.dropbox.model.enums.FileType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
public class File extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private Audit audit;

    //file key in mongo db
    @Column(name = "key", nullable = false)
    private String key;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_file_user"))
    private User user;

}
