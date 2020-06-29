package com.extia.crudExtia.services;

import com.extia.crudExtia.bo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    public List<User> getAllUsers() {
        return null;
    }

    public User getUser(Long id) {
        return User.builder()
                .id(1L).name("name").surname("surname")
                .build();
    }

    public List<User> findUsers(User search) {
        return null;
    }
}
