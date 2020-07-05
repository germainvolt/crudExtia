package com.extia.crudExtia.controller;

import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.models.User;
import com.extia.crudExtia.services.LibraryService;
import com.extia.crudExtia.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;
    @MockBean
    LibraryService libraryService;

    private List<User> users = new ArrayList<>();
    private User user;
    private List<Library> libraries = new ArrayList<>();
    private Library library;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks( this );
        library = Library.builder().libraryId(2L).name("library").userId(1L).build();
        libraries.add(library);
        user =User.builder().id(1L).name("name").lastname("lastname").libraries(libraries).build();
        users.add(user);

    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users2 = new ArrayList<>();
        users2.add(user);
        users2.add(User.builder().id(2L).name("name").lastname("lastname").build());
        given(userService.getAllUsers()).willReturn(users2);

        mvc.perform( MockMvcRequestBuilders
                .get("/users/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2L));

    }

    @Test
    void findUsers()throws Exception {
        given(userService.findUsers(any(User.class))).willReturn(users);
        mvc.perform( MockMvcRequestBuilders
                .post("/users/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(User.builder().id(2L).name("name").lastname("lastname").build()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L));

    }

    @Test
    void getUser() throws Exception {
        given(userService.getUser(1L)).willReturn(user);
        mvc.perform( MockMvcRequestBuilders
                .get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

    }

    @Test
    void updateUsers() throws Exception{
        given(userService.updateUsers(any(User.class))).willReturn(user);
        mvc.perform( MockMvcRequestBuilders
                .put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));


    }

    @Test
    void createUsers() throws Exception{
        given(userService.createUsers(any())).willReturn(user);

        mvc.perform( MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(User.builder().libraries(libraries).name("name").lastname("lastname").build()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.libraries").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.libraries[0].libraryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .delete("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserLibraries() throws Exception {
        given(libraryService.getLibrariesByUserId(1L)).willReturn(libraries);
        mvc.perform( MockMvcRequestBuilders
                .get("/users/1/libraries")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].libraryId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].libraryId").value(2L));
    }
}