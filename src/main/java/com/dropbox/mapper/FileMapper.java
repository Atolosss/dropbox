package com.dropbox.mapper;

import com.dropbox.model.entity.File;
import com.dropbox.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.gmm.demo.model.api.FilePatchRq;
import ru.gmm.demo.model.api.FileRs;
import ru.gmm.demo.model.api.FileUploadRq;

@Mapper(config = MapperConfiguration.class)
public interface FileMapper {

    String ID = "id";
    String NAME = "name";
    String URL = "url";

    default File toFile(final FileUploadRq fileUploadRq, final User user) {
        return File.builder()
            .name(fileUploadRq.getName())
            .user(user)
            .build();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = ID, source = ID)
    @Mapping(target = NAME, source = NAME)
    @Mapping(target = URL, source = URL)
    FileRs toFileRs(File file);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = NAME, source = NAME)
    @Mapping(target = URL, source = URL)
    void update(@MappingTarget File file, FilePatchRq userFilePatchRq);

}
