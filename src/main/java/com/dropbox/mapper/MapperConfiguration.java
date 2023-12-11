package com.dropbox.mapper;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;

@MapperConfig(
        componentModel = "spring",
        builder = @Builder(
                disableBuilder = true
        ),
        implementationPackage = "<PACKAGE_NAME>.impl",
        implementationName = "<CLASS_NAME>Impl"
)
public interface MapperConfiguration {
}
