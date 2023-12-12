package com.dropbox.mapper;

import com.dropbox.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.gmm.demo.model.api.UserPatchRq;
import ru.gmm.demo.model.api.UserPatchRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    User toUser(UserRegistrationRq userRegistrationRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "id", source = "id")
    UserRegistrationRs toUserRegistrationRs(User inserted);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "mapDateOfBirth")
    void update(@MappingTarget User user, UserPatchRq userPatchRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    UserPatchRs toUserPatchRs(User saved);

    @Named("mapDateOfBirth")
    default LocalDate mapDateOfBirth(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, FORMATTER);
    }
}
