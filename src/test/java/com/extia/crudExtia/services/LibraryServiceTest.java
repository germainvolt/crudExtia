package com.extia.crudExtia.services;


import com.extia.crudExtia.dao.LibraryDaoImpl;
import com.extia.crudExtia.enums.E_TypeItem;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import com.extia.crudExtia.models.Library;
import com.extia.crudExtia.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LibraryServiceTest {

    @InjectMocks
    LibraryService libraryService;

    @Mock
    LibraryDaoImpl libraryDao;
    @Mock
    ItemService itemService;

    private Library lib;
    private List<Library> libraries;
    private Map<Long, List<Item>> itemsMap;
    private Item item ;
    private User user;

    @BeforeEach
    public void init(){
        libraryDao = mock(LibraryDaoImpl.class);
        itemService = mock(ItemService.class);
        libraryService = new LibraryService();
        MockitoAnnotations.initMocks( this );

        item = Item.builder().itemId(6L).libraryId(5L).build();
        lib = Library.builder().libraryId(5L).name("library").items(newArrayList(item)).userId(1L).build();
        user =User.builder().id(1L).name("name").lastname("lastname").build();
        Map<Long, List<Item>> itemsMap = new HashMap<>();

        libraries = new ArrayList<>();
        libraries.add(lib);
        List<Item> items= newArrayList(item);
        itemsMap.put(5L,items);

        when(libraryDao.getLibraryByUsers(anyList())).thenReturn(newArrayList(libraries));
        when(libraryDao.getLibrary(5L)).thenReturn(Optional.ofNullable(lib));
        when(itemService.getItemByLibraries(anyList())).thenReturn(itemsMap);
        when(itemService.getItemsByLibrary(anyLong())).thenReturn(items);

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
        when(libraryDao.getLibrary(5L)).thenReturn(Optional.ofNullable(lib));
        Library res = libraryService.getLibrary(5L);

        assertNotNull(res);
        assertEquals(5L,res.getLibraryId());
    }
    @Test
    public void shouldThrowResourceNotFoundException() {
        when(libraryDao.getLibrary(1L)).thenReturn(Optional.empty());

        try {
            libraryService.getLibrary(1L);
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }

    }

    @Test
    public  void findLibraries() throws ResourceNotFoundException {
        when(libraryDao.findLibraries(lib)).thenReturn(libraries);

        List<Library> libs = libraryService.findLibraries(lib);
        assertNotNull(libs);
        assertTrue(libs.size()>0);
        assertEquals(5L,libs.get(0).getLibraryId());

    }

    @Test
    void getLibrariesByUserId() {

        List<Library> result = libraryService.getLibrariesByUserId(1L);
        assertNotNull(result);
        assertTrue(result.size()>0);
    }

    @Test
    void getMapLibrariesByUserIds() {

        Map<Long, List<Library>> mapLibrariesByUserIds = libraryService.getMapLibrariesByUserIds(newArrayList(1L));
        assertNotNull(mapLibrariesByUserIds);
        assertNotNull(mapLibrariesByUserIds.get(1L));
    }

    @Test
    void createOrUpdateLibraries() throws ResourceNotFoundException {

        List<Library> librariesUpdate = newArrayList(lib);

        Library libr = Library.builder().userId(1L).name("libr").build();
        librariesUpdate.add(libr);
        Item item1 = Item.builder().author("toto").name("Afrika").type(E_TypeItem.CD.getValue()).build();
        Library libr2 = Library.builder().userId(1L).name("libr").items(newArrayList(item)).build();
        librariesUpdate.add(libr2);

        when(itemService.getItemsByLibrary(6L)).thenReturn(newArrayList(item1));
        when(itemService.getItemsByLibrary(7L)).thenReturn(null);
        when(itemService.getItemsByLibrary(5L)).thenReturn(newArrayList(item));


        when(libraryDao.createLibrary(libr))
                .thenReturn(Library.builder().libraryId(7L).userId(1L).name("libr").build());
        when(libraryDao.createLibrary(libr2))
                .thenReturn(Library.builder().libraryId(6L).userId(1L).name("libr").items(newArrayList(item)).build());

        when(libraryDao.updateLibrary(lib)).thenReturn(lib);

        List<Library> updateLibraries = libraryService.createOrUpdateLibraries(librariesUpdate);
        assertNotNull(updateLibraries);
        assertTrue(updateLibraries.size()>0);

        verify(itemService,times(3)).updateOrCreateItems(anyList());
        verify(libraryDao,times(2)).createLibrary(any());
        verify(libraryDao,times(1)).updateLibrary(any());


    }

    @Test
    void createLibrary() {
        Library libr = Library.builder().libraryId(7L).userId(1L).items(newArrayList(item)).name("libr").build();
        when(itemService.updateOrCreateItems(anyList())).thenReturn(newArrayList(item));
        when(libraryDao.createLibrary(libr)).thenReturn(libr);
        Library library = libraryService.createLibrary(libr);
        assertNotNull(library);
    }

    @Test
    void updateLibrary() throws ResourceNotFoundException {
        when(itemService.updateOrCreateItems(anyList())).thenReturn(newArrayList(item));
        when(libraryDao.updateLibrary(lib)).thenReturn(lib);
        Library library = libraryService.updateLibrary(lib);
        assertNotNull(library);

    }

    @Test
    void deleteLibrary()  {

        when(itemService.getItemsByLibrary(5L)).thenReturn(newArrayList(item));
        libraryService.deleteLibrary(5L);
        verify(libraryDao,times(1)).deleteLibrary(5L);
        verify(itemService,times(1)).deleteItem(any(Item.class));
    }

}