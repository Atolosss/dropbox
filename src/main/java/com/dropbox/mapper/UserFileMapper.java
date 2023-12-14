package com.dropbox.mapper;

import com.dropbox.model.entity.UserFile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.gmm.demo.model.api.UserFilePatchRq;
import ru.gmm.demo.model.api.UserFilePatchRs;
import ru.gmm.demo.model.api.UserFileRs;
import ru.gmm.demo.model.api.UserFileUploadRq;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface UserFileMapper {
    @Named("mapUserFileRsList")
    List<UserFileRs> toUserFileRsList(List<UserFile> userFiles);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "user.id", source = "user")
    UserFile toUserFile(UserFileUploadRq userFileUploadRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    UserFileRs toUserFileRs(UserFile userFile);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    void update(@MappingTarget UserFile userFile, UserFilePatchRq userFilePatchRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    UserFilePatchRs toUserFilePatchRs(UserFile saved);
}
