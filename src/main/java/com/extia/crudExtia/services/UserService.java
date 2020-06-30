package com.extia.crudExtia.services;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;
import com.extia.crudExtia.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public User getUser(Long id) throws ResourceNotFoundException {

        return dao.getUser(id);
    }

    public List<User> findUsers(User search) throws ResourceNotFoundException {
        List<User> users = dao.findUsers(search);
        return users;
    }
}
