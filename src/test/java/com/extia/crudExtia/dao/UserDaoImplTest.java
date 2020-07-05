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
    void getUser() throws ResourceNotFoundException {

        User user = userDao.getUser(1L);
        assertNotNull(user);
        assertEquals(1L,user.getId());

        try {
            userDao.getUser(-11L);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
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
    void updateUser() throws ResourceNotFoundException {
        User user =User.builder().id(3L).name("Kamilah").lastname("Burden").build();
        User before = userDao.getUser(user.getId());
        assertNotEquals(user.getLastname(),before.getLastname());
        userDao.updateUser(user);
        User after = userDao.getUser(user.getId());
        assertEquals(user.getLastname(),after.getLastname());
    }

    @Test
    void deleteUser() throws ResourceNotFoundException {
        assertNotNull(userDao.getUser(11L));
        userDao.deleteUser(11L);
        try {
            userDao.getUser(11L);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
    }
}