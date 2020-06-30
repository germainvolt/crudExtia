package com.extia.crudExtia.dao;

import com.extia.crudExtia.bo.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUser(Long id);

    List<User> findUsers(User search);
}
