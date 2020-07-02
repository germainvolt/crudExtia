package com.extia.crudExtia.controller;


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
                    response = User.class,
                    responseContainer = "List")
    @RequestMapping(value="/all", method=RequestMethod.GET, produces = "application/json")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }

    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User corresponding to the search,, place '%' at the beginning or at the end of the field to search on part of the names",
                    httpMethod = "POST",
                    response = User.class,
                    responseContainer = "List")
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
        if(id==null){
            throw new Exception("Id can't be null");
        }
        return userService.getUser(id);
    }

    @ApiOperation
            (value = "Update user",
                    notes = "Return the updated User",
                    httpMethod = "PUT",
                    response = User.class)
    @RequestMapping(value="/{id}",method=RequestMethod.PUT, produces = "application/json")
    public User updateUsers(@PathVariable("id") Long id,@RequestBody User userToUpdate) throws Exception {
        if(userToUpdate==null){
            throw new Exception("user can't be null");
        }
        if(id!=userToUpdate.getId()){
            throw new Exception("Ids doesn't match");
        }
        return userService.UpdateUsers(userToUpdate);
    }
    @ApiOperation
            (value = "Create users",
                    notes = "Return the new user with ID",
                    httpMethod = "POST",
                    response = User.class)
    @RequestMapping(method=RequestMethod.POST, produces = "application/json")
    public User createUsers(@RequestBody User userToCreate) throws Exception {
        if(userToCreate==null){
            throw new Exception("new user can't be null");
        }
        return userService.createUsers(userToCreate);
    }
    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User corresponding to the search,, place '%' at the beginning or at the end of the field to search on part of the names",
                    httpMethod = "DELETE",
                    response = User.class,
                    responseContainer = "List")
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE, produces = "application/json")
    public void deleteUser(@PathVariable("id") Long id) throws Exception {
        if(id==null){
            throw new Exception("Id can't be null");
        }
        userService.deleteUser(id);
    }



}
