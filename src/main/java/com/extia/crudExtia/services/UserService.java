package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.LibraryDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.models.User;
import com.extia.crudExtia.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    LibraryDao libraryDao;

    private User user;

    public List<User> getAllUsers() {
        List<User> users=  userDao.getAllUsers();
        Map<Long, List<Library>> libraryByUsers = libraryDao.getLibraryByUsers(users);

        users.stream().forEach(user -> user.setLibraries(libraryByUsers.get(user.getId())));
        return users;
    }

    public User getUser(Long id) throws ResourceNotFoundException {

        user = userDao.getUser(id);
        user.setLibraries(libraryDao.findLibraries(Library.builder().userId(user.getId()).build()));
        return user;
    }

    public List<User> findUsers(User search) throws ResourceNotFoundException {
        List<User> users = userDao.findUsers(search);
        Map<Long, List<Library>> libraryByUsers = libraryDao.getLibraryByUsers(users);

        users.stream().forEach(user -> user.setLibraries(libraryByUsers.get(user.getId())));

        return users;
    }
}
