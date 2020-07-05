package com.extia.crudExtia.controller;

import com.extia.crudExtia.enums.E_TypeItem;
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
            (value = "Get all Item",
                    notes = "Return a list of Item",
                    httpMethod = "GET",
                    response = Item.class,
                    responseContainer = "List")
    @RequestMapping(value="/all", method= RequestMethod.GET, produces = "application/json")
    public List<Item> getAllItems(){
        List<Item> allItem = itemService.getAllItem();
        return allItem;
    }

    @ApiOperation
            (value = "Get one Item by id",
                    notes = "Return one Item",
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
            (value = "Get all items",
                    notes = "Return a list of items",
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


    @ApiOperation
            (value = "Create item",
                    notes = "Return new item with id, item type limited to BD,COMIC,MANGA,DVD,BLURAY,CD",
                    httpMethod = "POST",
                    response = Item.class)
    @RequestMapping(method= RequestMethod.POST, produces = "application/json")
    public Item createItem(@RequestBody Item itemToCreate) throws Exception {
        if(itemToCreate==null){
            throw new Exception("New item can't be null");
        }
        return itemService.createItem(itemToCreate);
    }

    @ApiOperation
            (value = "update item",
                    notes = "Return updated item. Item type limited to BD,COMIC,MANGA,DVD,BLURAY,CD",
                    httpMethod = "PUT",
                    response = Item.class)
    @RequestMapping(value="/{id}",method= RequestMethod.PUT, produces = "application/json")
    public Item editItem(@PathVariable("id") Long id,@RequestBody Item itemToEdit) throws Exception {
        if(itemToEdit==null){
            throw new Exception("New item can't be null");
        }
        if(id!=itemToEdit.getItemId()){
            throw new Exception("Items id don't match");
        }
        return itemService.checkAndUpdateItem(itemToEdit);
    }


    @ApiOperation
            (value = "Delete one Item by id",
                    notes = "",
                    httpMethod = "DELETE",
                    response = String.class)
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE, produces = "application/json")
    public void deleteItem(@PathVariable("id") Long id) throws Exception {
        if(id==null){
            throw new Exception("Id can't be null");
        }

        itemService.deleteItem(id);

    }
}
