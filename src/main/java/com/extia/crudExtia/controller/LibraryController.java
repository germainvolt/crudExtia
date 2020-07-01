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


    @RequestMapping(value="/search",method=RequestMethod.POST, produces = "application/json")
    public  List<Library> findLibraries(@RequestBody Library search) throws Exception {
        if(search==null){
            throw new Exception("search item can't be null");
        }
        return libraryService.findLibraries(search);
    }



}
