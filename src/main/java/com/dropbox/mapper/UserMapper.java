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
import ru.gmm.demo.model.api.UserRs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = MapperConfiguration.class,
    uses = FileMapper.class)
public interface UserMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String EMAIL = "email";
    String PASSWORD = "password";
    String ID = "id";
    String NAME = "firstName";
    String NAME1 = "lastName";
    String DATE_OF_BIRTH = "dateOfBirth";

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = EMAIL, source = EMAIL)
    @Mapping(target = PASSWORD, source = PASSWORD)
    User toUser(UserRegistrationRq userRegistrationRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = EMAIL, source = EMAIL)
    @Mapping(target = ID, source = ID)
    UserRegistrationRs toUserRegistrationRs(User inserted);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = NAME, source = NAME)
    @Mapping(target = NAME1, source = NAME1)
    @Mapping(target = DATE_OF_BIRTH, source = DATE_OF_BIRTH, qualifiedByName = "mapDateOfBirth")
    void update(@MappingTarget User user, UserPatchRq userPatchRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = ID, source = ID)
    @Mapping(target = EMAIL, source = EMAIL)
    @Mapping(target = NAME, source = NAME)
    @Mapping(target = NAME1, source = NAME1)
    @Mapping(target = DATE_OF_BIRTH, source = DATE_OF_BIRTH)
    UserPatchRs toUserPatchRs(User saved);

    @Named("mapDateOfBirth")
    default LocalDate mapDateOfBirth(final String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, FORMATTER);
    }

    @Named("mapUserRsList")
    List<UserRs> toUserRsList(List<User> users);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = ID, source = ID)
    @Mapping(target = EMAIL, source = EMAIL)
    @Mapping(target = NAME, source = NAME)
    @Mapping(target = NAME1, source = NAME1)
    @Mapping(target = DATE_OF_BIRTH, source = DATE_OF_BIRTH)
    UserRs toUserRs(User user);
}
