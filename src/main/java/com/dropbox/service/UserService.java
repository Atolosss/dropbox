package com.dropbox.service;

import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.UserMapper;
import com.dropbox.model.entity.User;
import com.dropbox.model.openapi.UserPatchRq;
import com.dropbox.model.openapi.UserPatchRs;
import com.dropbox.model.openapi.UserRegistrationRq;
import com.dropbox.model.openapi.UserRegistrationRs;
import com.dropbox.model.openapi.UserRs;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRegistrationRs createUser(final UserRegistrationRq userRegistrationRq) {
//        if (userRepository.findUserByEmail(userRegistrationRq.getEmail()).isPresent()) {
//            throw new ServiceException(ErrorCode.ERR_CODE_001, userRegistrationRq.getEmail());
//        }

        final User inserted = userRepository.save(userMapper.toUser(userRegistrationRq));
        return userMapper.toUserRegistrationRs(inserted);
    }

    public UserPatchRs patchUser(final Long id, final UserPatchRq userPatchRq) {
        final User user = userRepository.findById(id)
            .orElseThrow();
        return userMapper.toUserPatchRs(userRepository.save(userMapper.update(user, userPatchRq)));
    }

    public void deleteUser(final Long id) {
        final User user = userRepository.findById(id)
            .orElseThrow();
        userRepository.delete(user);
    }

    public List<UserRs> getAllUsers() {
        final List<User> users = userRepository.findAll();
        return userMapper.toUserRsList(users);
    }

    public UserRs getUser(final Long id) {

        final User user = userRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, "User with this id %s not found"));
        return userMapper.toUserRs(user);
    }

}
