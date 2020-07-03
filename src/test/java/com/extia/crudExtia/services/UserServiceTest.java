package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.UserDaoImpl;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {


    @Mock
    UserDaoImpl userDao;

    @Mock
    LibraryService libraryService;

    @InjectMocks
    UserService userService;

    Library library;
    List<Library> libraries;
    User user;
    Map<Long, List<Library>> libraryByUsers = new HashMap<>();

    @BeforeEach
    public void init(){
        userDao = mock(UserDaoImpl.class);
        libraryService = mock(LibraryService.class);
        userService = new UserService();
        MockitoAnnotations.initMocks( this );
        library = Library.builder().libraryId(2L).name("library").userId(1L).build();
        libraries = newArrayList(library);
        user =User.builder().id(1L).name("name").lastname("lastname").build();
    }

    @Test
    void shouldGetAllUsers() throws ResourceNotFoundException {
        when(userDao.getAllUsers()).thenReturn(newArrayList(user));
        //when(libraryService.getLibrariesByUserId(1L)).thenReturn(libraries);
        libraryByUsers.put(1L,libraries);
        when(libraryService.getMapLibrariesByUserIds(anyList())).thenReturn(libraryByUsers);

        List<User> allUsers = userService.getAllUsers();
        User userTest =allUsers.get(0);
        assertEquals(1L,user.getId());
        assertEquals("name",user.getName());
        assertNotNull(userTest.getLibraries());
        assertNotNull(userTest.getLibraries().get(0));
        Library libraryTest = userTest.getLibraries().get(0);
        assertEquals("library", libraryTest.getName());

    }

    @Test
    void shouldGetUser() throws ResourceNotFoundException {

        when(libraryService.getLibrariesByUserId(1L)).thenReturn(libraries);
        when(userDao.getUser(1L)).thenReturn(user);


        User userTest = userService.getUser(1L);
        assertEquals(1L,user.getId());
        assertEquals("name",user.getName());
        assertNotNull(userTest.getLibraries());
        assertNotNull(userTest.getLibraries().get(0));
        Library libraryTest = userTest.getLibraries().get(0);
        assertEquals("library", libraryTest.getName());
    }

    @Test
    void findUsers() throws ResourceNotFoundException {
        User userfind =User.builder().name("name").build();
        when(userDao.findUsers(userfind)).thenReturn(newArrayList(user));
        libraryByUsers.put(1L,libraries);
        when(libraryService.getMapLibrariesByUserIds(anyList())).thenReturn(libraryByUsers);

        List<User> testList = userService.findUsers(userfind);
        User userTest = testList.get(0);
        assertEquals(1L,user.getId());
        assertEquals("name",user.getName());
        assertNotNull(userTest.getLibraries());
        assertNotNull(userTest.getLibraries().get(0));
        Library libraryTest = userTest.getLibraries().get(0);
        assertEquals("library", libraryTest.getName());

    }

    @Test
    void updateUsers() {
    }

    @Test
    void createUsers() {
    }

    @Test
    void deleteUser() {
    }
}