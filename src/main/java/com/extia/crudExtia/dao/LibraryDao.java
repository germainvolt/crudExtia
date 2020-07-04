package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;

import java.util.List;

public interface LibraryDao {


    List<Library> getAllLibraries();

    Library getLibrary(Long id) throws ResourceNotFoundException;

    List<Library> findLibraries(Library search) throws ResourceNotFoundException;

    List<Library> getLibraryByUsers(List<Long> ids);

    Library createLibrary(Library libraryToCreate);

    Library updateLibrary(Library libraryToUpdate);

    void deleteLibrary(Long id);
}
