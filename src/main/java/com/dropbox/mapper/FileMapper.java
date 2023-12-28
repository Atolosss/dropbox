package com.dropbox.mapper;

import com.dropbox.model.dto.UploadFileRs;
import com.dropbox.model.openapi.FileRs;
import com.dropbox.model.openapi.FileUploadRq;
import com.dropbox.model.openapi.UploadFileDtoRs;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface FileMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "fileUploadRq.name")
    @Mapping(target = "url", source = "uploadFileRs.key")
    FileRs toFileRs(UploadFileRs uploadFileRs, FileUploadRq fileUploadRq);

    default UploadFileDtoRs toUploadFileDtoRs(final String encoded) {
        return UploadFileDtoRs.builder()
            .fileData(encoded)
            .build();
    }
}
