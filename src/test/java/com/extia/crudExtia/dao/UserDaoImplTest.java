package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@SpringBootTest
class UserDaoImplTest {

    @Autowired
    UserDaoImpl userDao;

    @Test
    void getAllUsers() {

        List<User> allUsers = userDao.getAllUsers();
        assertNotNull(allUsers);
        assertTrue(allUsers.size()>0);
        assertNotNull(allUsers.get(0));
    }

    @Test
    void getUser(){

        Optional<User> optionalUser = userDao.getUser(1L);
        assertTrue(optionalUser.isPresent());
        assertNotNull(optionalUser.get());
        assertEquals(1L,optionalUser.get().getId());

        optionalUser = userDao.getUser(-11L);
        assertFalse(optionalUser.isPresent());

        }

    @Test
    void findUsers() throws ResourceNotFoundException {
        User user = User.builder().lastname("%a%").build();
        List<User> users = userDao.findUsers(user);
        assertNotNull(users);
        assertNotNull(users.get(0));
        assertTrue(users.get(0).getLastname().contains("a"));

        user = User.builder().lastname("Diable").build();
        users = userDao.findUsers(user);
        assertNotNull(users);
        assertNotNull(users.get(0));
        assertEquals("Diable",users.get(0).getLastname());

        user = User.builder().name("%a%").build();
        users = userDao.findUsers(user);
        assertNotNull(users);
        assertNotNull(users.get(0));
        assertTrue(users.get(0).getName().contains("a"));

        user = User.builder().name("Lark").build();
        users = userDao.findUsers(user);
        assertNotNull(users);
        assertNotNull(users.get(0));
        assertEquals("Lark",users.get(0).getName());

        user = User.builder().id(5L).build();
        users = userDao.findUsers(user);
        assertNotNull(users);
        assertNotNull(users.get(0));
        assertEquals(5L,users.get(0).getId());
        try {
            user = User.builder().name("454").build();
            userDao.findUsers(user);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }

    }

    @Test
    void createUser() {
        User user =User.builder().name("name").lastname("lastname").build();
        User userCreated = userDao.createUser(user);
        assertNotNull(userCreated);
        assertNotNull(userCreated.getId());

    }

    @Test
    void updateUser() {
        User user =User.builder().id(3L).name("Kamilah").lastname("Burden").build();
        Optional<User> optionalBefore = userDao.getUser(user.getId());
        assertNotEquals(user.getLastname(), optionalBefore.get().getLastname());
        userDao.updateUser(user);
        Optional<User> optionalUser = userDao.getUser(user.getId());
        User after = optionalUser.get();
        assertEquals(user.getLastname(),after.getLastname());
    }

    @Test
    void deleteUser() {
        assertTrue(userDao.getUser(11L).isPresent());
        userDao.deleteUser(11L);
        assertFalse( userDao.getUser(11L).isPresent());
    }
}