package com.dropbox.mapper;

import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.UploadFileDtoRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface FileMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "uploadFileDtoRq.name")
    @Mapping(target = "key", source = "uploadFileRs.key")
    FileRs toFileRs(UploadFileDtoRq uploadFileDtoRq, UploadFileRs uploadFileRs);

    default UploadFileDtoRs toUploadFileDtoRs(final String encoded) {
        return UploadFileDtoRs.builder()
            .base64(encoded)
            .build();
    }
}
