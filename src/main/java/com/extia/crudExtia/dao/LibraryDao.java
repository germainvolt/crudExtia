package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.models.User;

import java.util.List;
import java.util.Map;

public interface LibraryDao {


    List<Library> getAllLibraries();

    Library getLibrary(Long id) throws ResourceNotFoundException;

    List<Library> findLibraries(Library search) throws ResourceNotFoundException;

    Map<Long, List<Library>> getLibraryByUsers(List<User> users);
}
