package com.IH.service;


import com.IH.model.User;
import com.IH.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(int id) {
        System.out.println("Ищу в базе ID: " + id);
        try {
            Optional<User> user = userRepository.getUserById(id);
            System.out.println("Нашел пользователя? " + user.isPresent());
            return user;
        } catch (Exception e) {
            System.err.println("ОШИБКА В СЕРВИСЕ: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
