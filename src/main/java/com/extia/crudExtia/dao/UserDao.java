package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAllUsers();

    Optional<User> getUser(Long id) throws ResourceNotFoundException;

    List<User> findUsers(User search) throws ResourceNotFoundException;

    User createUser(User userToCreate);

    User updateUser(User userToUpdate);

    void deleteUser(Long id);
}
