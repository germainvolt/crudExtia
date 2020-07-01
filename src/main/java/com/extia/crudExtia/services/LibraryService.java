package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.LibraryDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    @Autowired
    LibraryDao libraryDao;


    public List<Library> getAllLibraries() {

        return libraryDao.getAllLibraries();
    }

    public Library getLibrary(Long id) throws ResourceNotFoundException {
        return libraryDao.getLibrary(id);

    }

    public List<Library> findLibraries(Library search) throws ResourceNotFoundException {
        return libraryDao.findLibraries(search);
    }
}
