package com.extia.crudExtia.controller;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;
import com.extia.crudExtia.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/all", method=RequestMethod.GET, produces = "application/json")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }

    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/search",method=RequestMethod.POST, produces = "application/json")
    public List<User> findUsers(@RequestBody User search) throws Exception {
        if(search==null){
            throw new Exception("Search can't be null");
        }
        return userService.findUsers(search);
    }

    @ApiOperation
            (value = "Get one users",
                    notes = "Return one User from their id",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/{id}",method=RequestMethod.GET, produces = "application/json")
    public User getUser(@PathVariable("id") Long id) throws Exception {
        log.error("looking for user ",id);
        if(id==null){
            throw new Exception("Id can't be null");
        }
        return userService.getUser(id);
    }




}
