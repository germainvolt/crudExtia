package com.extia.crudExtia.controller;

import com.extia.crudExtia.bo.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/User")
public class UserController {

    @ApiOperation
            (value = "Get all users",
                    notes = "Return a list of User",
                    httpMethod = "GET",
                    response = User.class)
    @RequestMapping(value="/",method=RequestMethod.GET)
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User("Germain", "VOLT", 1l));
        return users;
    }
}
