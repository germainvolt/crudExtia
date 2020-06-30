package com.extia.crudExtia.controller;

import com.extia.crudExtia.CrudExtiaApplication;

import com.extia.crudExtia.bo.User;
import com.extia.crudExtia.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles({"test"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class UserControllerTest {

    @Mock
    UserService service;

    @Autowired
    private WebApplicationContext web;

    private MockMvc mock;

    private List<User> users= new ArrayList<>();
    private User user;

    @Before
    public void init() {

        mock = MockMvcBuilders.webAppContextSetup(web).build();
        user = User.builder()
                .id(1L).name("name").surname("surname")
                .build();
        users.add(user);

    }

    @Test
    public void should_return_list_of_users() throws Exception {
        when(service.getAllUsers()).thenReturn(users);
        MockHttpServletResponse response = mock.perform(get("/user/all")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        log.debug(response.getContentAsString());

    }

    @Test
    public void should_return_one_user_by_id() throws Exception {
        when(service.getUser(1L)).thenReturn(user);
        when(service.getUser(0L)).thenReturn(null);
        when(service.findUsers(any(User.class))).thenReturn(users);
        MvcResult mvcResult = mock.perform(get("/user/" + 0L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(200, response.getStatus());
        log.error(response.getContentAsString());

    }
}