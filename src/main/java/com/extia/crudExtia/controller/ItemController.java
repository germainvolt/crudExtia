package com.extia.crudExtia.controller;

import com.extia.crudExtia.models.Item;
import com.extia.crudExtia.services.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    @ApiOperation
            (value = "Get all libraries",
                    notes = "Return a list of libraries",
                    httpMethod = "GET",
                    response = Item.class,
                    responseContainer = "List")
    @RequestMapping(value="/all", method= RequestMethod.GET, produces = "application/json")
    public List<Item> getAllItems(){
        return itemService.getAllItem();
    }

    @ApiOperation
            (value = "Get all libraries",
                    notes = "Return a list of libraries",
                    httpMethod = "GET",
                    response = Item.class)
    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = "application/json")
    public Item getItem(@PathVariable("id") Long id) throws Exception {
        if(id==null){
            throw new Exception("Id can't be null");
        }
        return itemService.getItem(id);

    }

    @ApiOperation
            (value = "Get all libraries",
                    notes = "Return a list of libraries",
                    httpMethod = "POST",
                    response = Item.class,
                    responseContainer = "List")
    @RequestMapping(value="/search", method= RequestMethod.POST, produces = "application/json")
    public List<Item> findItem(@RequestBody Item search) throws Exception {
        if(search==null){
            throw new Exception("search item can't be null");
        }
        return itemService.searchItem(search);

    }


}
