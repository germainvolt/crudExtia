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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {


    @Mock
    UserDaoImpl userDao;

    @Mock
    LibraryService libraryService;

    @InjectMocks
    UserService userService;

    private Library library;
    private List<Library> libraries;
    private User user;
    private Map<Long, List<Library>> libraryByUsers = new HashMap<>();

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
    void updateUsers() throws ResourceNotFoundException{
        User userUp = User.builder().id(1L).lastname("new lastname").name("new name").build();
        when(userDao.updateUser(any(User.class))).thenReturn(userUp);
        when(libraryService.createOrUpdateLibraries(anyList())).thenReturn(null);
        when(userDao.getUser(1L)).thenReturn(user);

        User userTest = userService.updateUsers(userUp);
        assertNotNull(userTest);
        verify(userDao,times(1)).updateUser(any(User.class));
        verify(libraryService,times(0)).deleteLibrary(any(Library.class));
        verify(libraryService,times(1)).createOrUpdateLibraries(anyList());

    }
    @Test
    void updateUsersWithLibraryToDelete() throws ResourceNotFoundException{
        User userUp = User.builder().id(1L).lastname("new lastname").name("new name")
                .libraries(newArrayList(Library.builder().userId(1L).libraryId(5L).name("library").build()))
                .build();
        when(userDao.updateUser(any(User.class))).thenReturn(user);
        when(libraryService.getLibrariesByUserId(1L)).thenReturn(newArrayList(Library.builder().userId(1L).libraryId(5L).name("library").build()));
        when(libraryService.createOrUpdateLibraries(anyList())).thenReturn(null);
        when(userDao.getUser(1L)).thenReturn(userUp);

        User userTest = userService.updateUsers(user);
        assertNotNull(userTest);
        verify(userDao,times(1)).updateUser(any(User.class));
        verify(libraryService,times(1)).deleteLibrary(any(Library.class));
        verify(libraryService,times(1)).createOrUpdateLibraries(anyList());

    }

    @Test
    void createUsers() {
        Library library = Library.builder().userId(1L).libraryId(5L).name("library").build();
        User userUp = User.builder().id(1L).lastname("new lastname").name("new name")
                .libraries(newArrayList(library))
                .build();
        when(libraryService.createLibrary(library)).thenReturn(library);
        when(userDao.createUser(userUp)).thenReturn(userUp);
        userService.createUsers(userUp);

        verify(libraryService,times(1)).createLibrary(any());


    }

    @Test
    void createUsersNoLibrary() {
        Library library = Library.builder().userId(1L).libraryId(5L).name("library").build();
        User userUp = User.builder().id(1L).lastname("new lastname").name("new name")
                .build();
        when(userDao.createUser(userUp)).thenReturn(userUp);
        userService.createUsers(userUp);

        verify(libraryService,times(0)).createLibrary(any());


    }

    @Test
    void deleteUser() throws ResourceNotFoundException {
        when(userDao.getUser(1L)).thenReturn(user);
        userService.deleteUser(1L);

        verify(userDao,times(1)).deleteUser(1L);
    }
}