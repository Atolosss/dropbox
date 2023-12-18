package com.dropbox.mapper;

import com.dropbox.model.entity.File;
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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "userId", source = "userId")
    File toFile(FileUploadRq fileUploadRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    FileRs toFileRs(File file);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    void update(@MappingTarget File file, FilePatchRq userFilePatchRq);

}
