package com.extia.crudExtia.services;


import com.extia.crudExtia.dao.LibraryDaoImpl;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class LibraryServiceTest {
    @InjectMocks
    LibraryService libraryService;

    @Mock
    LibraryDaoImpl libraryDao;
    @Mock
    ItemService itemService;

    Library lib;
    List<Library> libraries;

    @BeforeEach
    public void init(){
        libraryDao = mock(LibraryDaoImpl.class);
        itemService = mock(ItemService.class);
        libraryService = new LibraryService();
        MockitoAnnotations.initMocks( this );

        lib = Library.builder().libraryId(5L).name("library").userId(1L).build();
        libraries = new ArrayList<>();
        libraries.add(lib);
    }

    @Test
    public void ShouldGetAllLibraries() {

        when(libraryDao.getAllLibraries()).thenReturn(libraries);
        List<Library> libs = libraryService.getAllLibraries();

        assertNotNull(libs);
        assertTrue(libs.size()>0);
        assertEquals(5L,libs.get(0).getLibraryId());

    }

    @Test
    public void shouldGetLibraryById() throws ResourceNotFoundException {
        when(libraryDao.getLibrary(5L)).thenReturn(lib);
        Library res = libraryService.getLibrary(5L);

        assertNotNull(res);
        assertEquals(5L,res.getLibraryId());
    }
    @Test
    public void shouldThrowResourceNotFoundException() throws ResourceNotFoundException {
        when(libraryDao.getLibrary(1L)).thenThrow(ResourceNotFoundException.class);

        try {
            libraryService.getLibrary(1L);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
 ;
    }

    @Test
    public  void findLibraries() throws ResourceNotFoundException {
        when(libraryDao.findLibraries(lib)).thenReturn(libraries);

        List<Library> libs = libraryService.findLibraries(lib);
        assertNotNull(libs);
        assertTrue(libs.size()>0);
        assertEquals(5L,libs.get(0).getLibraryId());

    }
}