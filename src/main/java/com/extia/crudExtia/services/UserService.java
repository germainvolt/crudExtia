package com.extia.crudExtia.services;

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
    LibraryService libraryService;


    public List<User> getAllUsers() {
        List<User> users=  userDao.getAllUsers();
        linkLibrariesToUser(users);
        return users;
    }

    public User getUser(Long id) throws ResourceNotFoundException {

        User user = userDao.getUser(id);
        List<Library> libraries =libraryService.getLibrariesByUserId(id);
        user.setLibraries(libraries);
        return user;
    }

    public List<User> findUsers(User search) throws ResourceNotFoundException {
        List<User> users = userDao.findUsers(search);
        linkLibrariesToUser(users);

        return users;
    }

    private void linkLibrariesToUser(List<User> users) {
        List<Long> ids= users.stream().map(User::getId).collect(Collectors.toList());
        Map<Long, List<Library>> libraryByUsers = libraryService.getMapLibrariesByUserIds(ids);

        users.stream().forEach(user -> user.setLibraries(libraryByUsers.get(user.getId())));
    }

    public User UpdateUsers(User userToUpdate) throws ResourceNotFoundException {
        Long userId = userToUpdate.getId();
        List<Library> libraries = getUser(userToUpdate.getId()).getLibraries();
        List<Long> ids= userToUpdate.getLibraries().stream().map(Library::getLibraryId).collect(Collectors.toList());

        libraries.stream()
                .filter(library -> !ids.contains(library.getLibraryId()))
                .forEach(library -> libraryService.deleteLibrary(library));

        libraries = libraryService.createOrUpdateLibraries(userToUpdate.getLibraries());
        userToUpdate= userDao.updateUser(userToUpdate);
        userToUpdate.setLibraries(libraries);
        return userToUpdate;
    }

    public User createUsers(User userToCreate) {
        User createdUser = userDao.createUser(userToCreate);
        createdUser.getLibraries().forEach(library -> {
            library.setUserId(createdUser.getId());
            libraryService.createLibrary(library);
        });
        return createdUser;
    }

    public void deleteUser(Long id) throws ResourceNotFoundException {
        User userToDelete=  getUser(id);
        userToDelete.getLibraries().forEach(library -> libraryService.deleteLibrary(library));
        userToDelete.setLibraries(null);
        userDao.deleteUser(id);

    }
}
