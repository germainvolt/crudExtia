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
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    LibraryDao libraryDao;

    @Autowired
    LibraryService libraryService;

    private User user;

    public List<User> getAllUsers() {
        List<User> users=  userDao.getAllUsers();
        linkLibrariesToUser(users);
        return users;
    }

    public User getUser(Long id) throws ResourceNotFoundException {

        user = userDao.getUser(id);

        Map<Long, List<Library>> libraryByUsers = libraryService.getLibraryByUsers(newArrayList(user.getId()));
        user.setLibraries(libraryByUsers.get(user.getId()));
        return user;
    }

    public List<User> findUsers(User search) throws ResourceNotFoundException {
        List<User> users = userDao.findUsers(search);
        linkLibrariesToUser(users);

        return users;
    }

    private void linkLibrariesToUser(List<User> users) {
        List<Long> ids= users.stream().map(User::getId).collect(Collectors.toList());
        Map<Long, List<Library>> libraryByUsers = libraryService.getLibraryByUsers(ids);

        users.stream().forEach(user -> user.setLibraries(libraryByUsers.get(user.getId())));
    }
}
