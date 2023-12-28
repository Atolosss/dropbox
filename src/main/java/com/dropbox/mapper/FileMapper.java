package com.dropbox.mapper;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.FilePatchRq;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfiguration.class)
public interface FileMapper {

    default File toFile(final FileUploadRq fileUploadRq, final User user, final String key) {
        return File.builder()
            .name(fileUploadRq.getName())
            .user(user)
            .key(key)
            .build();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "key")
    FileRs toFileRs(File file);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "key", source = "url")
    File update(@MappingTarget File file, FilePatchRq userFilePatchRq);

    default UploadFileDtoRs toUploadFileDtoRs(final File file, final String s) {
        return UploadFileDtoRs.builder()
            .name(file.getName())
            .fileData(s)
            .build();
    }
}
