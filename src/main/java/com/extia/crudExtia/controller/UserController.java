package com.extia.crudExtia.controller;

import com.extia.crudExtia.bo.User;
import com.extia.crudExtia.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @RequestMapping(value="/all", method=RequestMethod.GET)
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }

    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/search",method=RequestMethod.POST)
    public List<User> findUsers(@RequestBody User search){

        return userService.findUsers(search);
    }

    @ApiOperation
            (value = "Get one users",
                    notes = "Return one User from their id",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public User getUser(@PathVariable("id") Long id){

        return userService.getUser(id);
    }




}
