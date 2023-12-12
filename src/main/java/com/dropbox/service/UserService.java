package com.dropbox.service;

import com.dropbox.constant.ErrorCode;
import com.dropbox.exceptions.ServiceException;
import com.dropbox.mapper.UserMapper;
import com.dropbox.model.entity.User;
import com.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gmm.demo.model.api.UserPatchRq;
import ru.gmm.demo.model.api.UserPatchRs;
import ru.gmm.demo.model.api.UserRegistrationRq;
import ru.gmm.demo.model.api.UserRegistrationRs;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRegistrationRs createUser(final UserRegistrationRq userRegistrationRq) {
        if (userRepository.findUserByEmail(userRegistrationRq.getEmail()).isPresent()) {
            throw new ServiceException(ErrorCode.ERR_CODE_001, userRegistrationRq.getEmail());
        }

        final User user = userMapper.toUser(userRegistrationRq);
        final User inserted = userRepository.save(user);
        return userMapper.toUserRegistrationRs(inserted);
    }

    public UserPatchRs patchUser(final String id, final UserPatchRq userPatchRq) {
        final User user = userRepository.findById(id)
                .orElseThrow();
        userMapper.update(user, userPatchRq);
        final User saved = userRepository.save(user);
        return userMapper.toUserPatchRs(saved);
    }
}
