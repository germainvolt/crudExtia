package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryDao {


    List<Library> getAllLibraries();

    Optional<Library> getLibrary(Long id) ;

    List<Library> findLibraries(Library search) throws ResourceNotFoundException;

    List<Library> getLibraryByUsers(List<Long> ids);

    Library createLibrary(Library libraryToCreate);

    Library updateLibrary(Library libraryToUpdate);

    void deleteLibrary(Long id);
}
