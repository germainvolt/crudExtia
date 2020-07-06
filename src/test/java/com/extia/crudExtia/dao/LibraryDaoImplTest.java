package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@SpringBootTest
class LibraryDaoImplTest {

    @Autowired
    LibraryDao libraryDao;

    @Test
    void getAllLibraries() {
        List<Library> allLibraries = libraryDao.getAllLibraries();
        assertNotNull(allLibraries);
        assertTrue(allLibraries.size()>0);
        assertNotNull(allLibraries.get(0));

    }

    @Test
    void getLibrary() throws ResourceNotFoundException {
        Optional<Library> optionalLibrary = libraryDao.getLibrary(1L);
        assertTrue(optionalLibrary.isPresent());
        Library library = optionalLibrary.get();
        assertNotNull(library);
        assertEquals(1L,library.getLibraryId());
    }

    @Test
    void dontGetLibrary() {
        Optional<Library> optionalLibrary = libraryDao.getLibrary(-1L);
        assertFalse(optionalLibrary.isPresent());
    }

    @Test
    void findLibraries() throws ResourceNotFoundException {
        Library library = Library.builder().userId(1L).build();
        List<Library> libraries = libraryDao.findLibraries(library);
        assertNotNull(libraries);
        assertTrue(libraries.size()>0);
        assertNotNull(libraries.get(0));
        assertEquals(1L,libraries.get(0).getUserId());

        library =Library.builder().libraryId(1L).build();
        libraries = libraryDao.findLibraries(library);
        assertNotNull(libraries);
        assertTrue(libraries.size()>0);
        assertNotNull(libraries.get(0));
        assertEquals(1L,libraries.get(0).getLibraryId());

        library = Library.builder().name("%a%").build();
        libraries = libraryDao.findLibraries(library);
        assertNotNull(libraries);
        assertTrue(libraries.size()>0);
        assertNotNull(libraries.get(0));
        assertTrue(libraries.get(0).getName().contains("a"));

        library =Library.builder().name("MacIlriach").build();
        libraries = libraryDao.findLibraries(library);
        assertNotNull(libraries);
        assertTrue(libraries.size()>0);
        assertNotNull(libraries.get(0));
        assertEquals("MacIlriach",libraries.get(0).getName());


        library =Library.builder().name("949645466").build();
        try {
            libraryDao.findLibraries(library);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
    }

    @Test
    void getLibraryByUsers() {
        List<Library> libraries = libraryDao.getLibraryByUsers(newArrayList(1L));
        assertNotNull(libraries);
        assertTrue(libraries.size()>0);
        assertNotNull(libraries.get(0));
        assertEquals(1L,libraries.get(0).getUserId());

    }

    @Test
    void createLibrary() {
        Library library = libraryDao.createLibrary(Library.builder().libraryId(5L).name("Mes Manga").userId(1L).build());
        assertNotNull(library);
        assertNotNull(library.getLibraryId());

    }

    @Test
    void updateLibrary()  {
        Library library = Library.builder().libraryId(1L).name("Mes Manga").userId(1L).build();
        libraryDao.updateLibrary(library);
        Optional<Library> optionalLibrary = libraryDao.getLibrary(1L);
        assertTrue(optionalLibrary.isPresent());
        Library libraryUpdated = optionalLibrary.get();
        assertEquals(library.getName(),libraryUpdated.getName());

    }

    @Test
    void deleteLibrary() {

        libraryDao.deleteLibrary(1L);

        Optional<Library> optionalLibrary = libraryDao.getLibrary(1L);
        assertFalse(optionalLibrary.isPresent());
    }
}