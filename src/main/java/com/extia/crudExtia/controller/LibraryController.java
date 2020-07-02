package com.extia.crudExtia.controller;

import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.services.LibraryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/libraries")
public class LibraryController {

    @Autowired
    LibraryService libraryService;



    @ApiOperation
            (value = "Get all libraries",
                    notes = "Return a list of libraries",
                    httpMethod = "GET",
                    response = Library.class,
                    responseContainer = "List")
    @RequestMapping(value="/all", method= RequestMethod.GET, produces = "application/json")
    public List<Library> getAllLibraries(){

        return libraryService.getAllLibraries();
    }
    @ApiOperation
            (value = "Get one library by its id",
                    notes = "Return a library",
                    httpMethod = "GET",
                    response = Library.class)
    @RequestMapping(value="/{id}", method= RequestMethod.GET, produces = "application/json")
    public Library getLibrary(@PathVariable("id") Long id) throws Exception {
        if(id==null){
            throw new Exception("Id can't be null");
        }
        return libraryService.getLibrary(id);

    }

    @ApiOperation
            (value = "Search libraries",
                    notes = "Return a list of libraries",
                    httpMethod = "POST",
                    response = Library.class,
                    responseContainer = "List")
    @RequestMapping(value="/search",method=RequestMethod.POST, produces = "application/json")
    public  List<Library> findLibraries(@RequestBody Library search) throws Exception {
        if(search==null){
            throw new Exception("search item can't be null");
        }
        return libraryService.findLibraries(search);
    }

    @ApiOperation
            (value = "Create a library",
                    notes = "Return new library with ID",
                    httpMethod = "POST",
                    response = Library.class)
    @RequestMapping(method=RequestMethod.POST, produces = "application/json")
    public  Library createLibrary(@RequestBody Library libraryToCreate) throws Exception {
        if(libraryToCreate==null){
            throw new Exception("new library can't be null");
        }
        return libraryService.createLibrary(libraryToCreate);
    }

    @ApiOperation
            (value = "update a library",
                    notes = "Return updated library ",
                    httpMethod = "PUT",
                    response = Library.class)
    @RequestMapping(value = "/{id}", method=RequestMethod.PUT, produces = "application/json")
    public  Library updateLibrary(@PathVariable("id") Long id,@RequestBody Library libraryToUpdate) throws Exception {
        if(libraryToUpdate==null){
            throw new Exception("new library can't be null");
        }
        if(id!=libraryToUpdate.getLibraryId()){
            throw new Exception("Ids don't match");
        }
        return libraryService.updateLibrary(libraryToUpdate);
    }


    @ApiOperation
            (value = "update a library",
                    notes = "Return updated library ",
                    httpMethod = "DELETE",
                    response = Library.class)
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public void deleteLibrary(@PathVariable("id") Long id) throws Exception {
        if(id==null){
            throw new Exception("Id can't be null");
        }
        libraryService.deleteLibrary(id);

    }
}
