package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.ItemDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ItemServiceTest {

    @InjectMocks
    ItemService itemService;

    @Mock
    ItemDao itemDao;

    private List<Item> items =new ArrayList<>();
    private Item item;


    @BeforeEach
    void setUp() throws ResourceNotFoundException {
        itemDao = mock(ItemDao.class);
        itemService = new ItemService();
        MockitoAnnotations.initMocks( this );
        item = Item.builder().itemId(6L).libraryId(5L).build();
        when(itemDao.getItem(6L)).thenReturn(item);
        items.add(item);
    }

    @Test
    void getAllItem() {
        when(itemDao.getAllItems()).thenReturn(items);
        List<Item> allItem = itemService.getAllItem();
        assertNotNull(allItem);
        assertTrue(allItem.size()>0);
    }

    @Test
    void getItem() throws ResourceNotFoundException {
        Item ite = itemService.getItem(6L);
        assertNotNull(ite);
        assertEquals(6L,ite.getItemId());
    }

    @Test
    void searchItem() throws ResourceNotFoundException {
        when(itemDao.searchItem(item)).thenReturn(items);

        List<Item> result = itemService.searchItem(item);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertEquals(6L,result.get(0).getItemId());

    }

    @Test
    void getItemsByLibrary() {
        when(itemDao.getItemByLibraries(anyList())).thenReturn(items);
        List<Item> result = itemService.getItemsByLibrary(5L);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertEquals(6L,result.get(0).getItemId());

    }

    @Test
    void createItem() {
        when(itemDao.createItem(item)).thenReturn(item);
        Item result = itemService.createItem(item);
        assertNotNull(result);
        assertEquals(6L,result.getItemId());
        verify(itemDao,times(1)).createItem(item);
    }

    @Test
    void checkAndUpdateItem() throws ResourceNotFoundException {
        when(itemDao.updateItem(item)).thenReturn(item);
        Item result = itemService.checkAndUpdateItem(item);
        assertNotNull(result);
        assertEquals(6L,result.getItemId());
        verify(itemDao,times(1)).updateItem(item);

    }

    @Test
    void updateOrCreateItems() {
        List<Item> itemsCU = new ArrayList<>();
        Item item2 = Item.builder().libraryId(1L).author("toto").build();
        itemsCU.add(item2);
        itemsCU.add(item);
        when(itemDao.updateItem(item)).thenReturn(item);
        when(itemDao.createItem(item2)).thenReturn(Item.builder().itemId(12L).libraryId(1L).author("toto").build());

        List<Item> itemList = itemService.updateOrCreateItems(itemsCU);
        assertNotNull(itemList);
        assertTrue(itemList.size()==2);
        verify(itemDao,times(1)).createItem(item2);
        verify(itemDao,times(1)).updateItem(item);

    }

    @Test
    void deleteItem() {
    }
}