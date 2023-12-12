package com.dropbox.service;

import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.UserMapper;
import com.dropbox.model.dto.AddUserRq;
import com.dropbox.model.dto.UpdateUserRq;
import com.dropbox.model.dto.UserRs;
import com.dropbox.model.entity.User;
import com.dropbox.model.enums.ErrorCode;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserRs> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserRs)
                .toList();
    }

    public UserRs createUser(final AddUserRq addUserRq) {
        final User user = userMapper.toUser(addUserRq);

        userRepository.findUserByEmail(user.getMail()).orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, user.getMail()));
        userRepository.insert(user);

        return userMapper.toUserRs(user);
    }

    public UserRs findByEmail(final String email) {
        final User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, email));

        return userMapper.toUserRs(user);
    }

    public UserRs updateUserByEmail(final UpdateUserRq updateUserRq, final String email) {
        final User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, email));

        userMapper.update(user, updateUserRq);

        return userMapper.toUserRs(user);
    }
}
