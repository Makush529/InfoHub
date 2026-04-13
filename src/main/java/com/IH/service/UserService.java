package com.IH.service;

import com.IH.model.dto.responce.UserDto;
import com.IH.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<UserDto> getUserById(long id) {
        try {
            Optional<UserDto> user = userRepository.getUserById(id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public int getUserRating(long userId) {
        try {
            return userRepository.getUserRating(userId);
        } catch (Exception e) {
            log.error("Error getting user rating", e);
            return 0;
        }
    }
}
