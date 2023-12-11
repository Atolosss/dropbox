package com.dropbox.mapper;

import com.dropbox.model.dto.AddUserRq;
import com.dropbox.model.dto.UpdateUserRq;
import com.dropbox.model.dto.UserRs;
import com.dropbox.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {
    User toUser(AddUserRq addUserRq);

    UserRs toUserRs(User user);

    void update(User user, UpdateUserRq updateUserRq);
}
