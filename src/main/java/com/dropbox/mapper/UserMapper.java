package com.dropbox.mapper;

import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.model.openapi.UserRegistrationRs;
import com.dropbox.model.openapi.UserRs;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfiguration.class,
    uses = FileMapper.class)
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "telegramUserId", source = "telegramUserId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    User toUser(UserRegistrationRq userRegistrationRq);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserRegistrationRs toUserRegistrationRs(User inserted);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    List<UserRs> toUserRsList(List<User> users);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserRs toUserRs(User user);
}
