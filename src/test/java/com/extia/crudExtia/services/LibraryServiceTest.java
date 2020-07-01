package com.extia.crudExtia.services;


import com.extia.crudExtia.dao.LibraryDaoImpl;
import com.extia.crudExtia.models.Library;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ActiveProfiles({"test"})
@RunWith(SpringJUnit4ClassRunner.class)
class LibraryServiceTest {
    @InjectMocks
    LibraryService libraryService;

    @Mock
    LibraryDaoImpl libraryDao;


    @Test
    void Should_get_All_Libraries() {
        Library lib = Library.builder().libraryId(5L).name("library").userId(1L).build();
        List<Library> libraries = new ArrayList<>();
        libraries.add(lib);
        when(libraryDao.getAllLibraries()).thenReturn(libraries);
        List<Library> libs = libraryService.getAllLibraries();

        assertThat(libs).isNotNull();
        assertThat(libs).isNotEmpty();
        assertThat(libs.get(0).getLibraryId()).isEqualTo(5L);

    }

    @Test
    void should_get_Library_by_id() {
        Library lib = Library.builder().libraryId(5L).name("library").userId(1L).build();
        when(libraryDao.getLibrary(5L)).thenReturn(lib);
        Library res = libraryService.getLibrary(5L);

        assertThat(res).isNotNull();
        assertThat(res.getLibraryId()).isEqualTo(5L);
    }
    @Test
    void should_not_get_Library_by_id() {
        when(libraryDao.getLibrary(1L)).thenReturn(null);
        Library res = libraryService.getLibrary(1L);

        assertThat(res).isNull();
    }

    @Test
    void findLibraries() {
        Library lib = Library.builder().libraryId(5L).name("library").userId(1L).build();
        List<Library> libraries = new ArrayList<>();
        libraries.add(lib);
        when(libraryDao.findLibraries(lib)).thenReturn(libraries);

        List<Library> libs = libraryService.findLibraries(lib);
        assertThat(libs).isNotNull();
        assertThat(libs).isNotEmpty();
        assertThat(libs.get(0).getLibraryId()).isEqualTo(5L);

    }
}