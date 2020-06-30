package com.extia.crudExtia.services;

import com.extia.crudExtia.bo.User;
import com.extia.crudExtia.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao dao;

    public List<User> getAllUsers() {
        List<User> users=  dao.getAllUsers();
        return users;
    }

    public User getUser(Long id) {
        dao.getUser(id);
        return User.builder()
                .id(1L).name("name").lastname("surname")
                .build();
    }

    public List<User> findUsers(User search) {
        List<User> users = dao.findUsers(search);
        return null;
    }
}
