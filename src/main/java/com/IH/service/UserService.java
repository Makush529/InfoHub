package com.IH.service;


import com.IH.model.dto.rest.UserDto;
import com.IH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
