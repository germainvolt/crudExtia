package com.extia.crudExtia.dao;

import com.extia.crudExtia.enums.E_TypeItem;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@Transactional
@Rollback
@SpringBootTest
class ItemDaoImplTest {

    @Autowired
    ItemDao itemDao;

    @Test
    void getAllItems() {
        List<Item> allItems = itemDao.getAllItems();
        assertNotNull(allItems);
        assertTrue(allItems.size()>0);
        assertNotNull(allItems.get(0));
    }

    @Test
    void getItem() throws ResourceNotFoundException {
        Optional<Item> item1 = itemDao.getItem(1L);
        assertTrue(item1.isPresent());
        Item item = item1.get();
        assertNotNull(item);
        assertEquals(1L,item.getItemId());
    }

    @Test
    void searchItem() {
        Item search= Item.builder().name("%a%").build();
        List<Item>  result = itemDao.searchItem(search);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertNotNull(result.get(0));
        assertTrue(result.get(0).getName().contains("a"));

        search= Item.builder().name("Trippledex").build();
        result = itemDao.searchItem(search);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertNotNull(result.get(0));
        assertEquals("Trippledex",result.get(0).getName());

        search= Item.builder().author("%a%").build();
        result = itemDao.searchItem(search);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertNotNull(result.get(0));
        assertTrue(result.get(0).getAuthor().contains("a"));

        search= Item.builder().author("Mariette Haxell").build();
        result = itemDao.searchItem(search);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertNotNull(result.get(0));
        assertEquals("Mariette Haxell",result.get(0).getAuthor());

        search= Item.builder().type(E_TypeItem.CD.getValue()).build();
        result = itemDao.searchItem(search);
        assertNotNull(result);
        assertTrue(result.size()>0);
        assertNotNull(result.get(0));
        assertEquals(E_TypeItem.CD.getValue(),result.get(0).getType());

    }

    @Test
    void getItemByLibraries() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<Item> itemByLibraries = itemDao.getItemByLibraries(ids);

        assertNotNull(itemByLibraries);
    }

    @Test
    void createItem() {
        Item item = Item.builder().name("name").libraryId(5L)
                .type(E_TypeItem.MANGA.getValue()).author("Leiji Matsumoto").build();
        Item created = itemDao.createItem(item);
        assertNotNull(created);
        assertNotNull(created.getItemId());
    }

    @Test
    void updateItem() throws ResourceNotFoundException {
        Item item = Item.builder().itemId(1L).name("name").libraryId(5L)
                .type(E_TypeItem.MANGA.getValue()).author("Leiji Matsumoto").build();
        itemDao.updateItem(item);
        Optional<Item> optionalItem = itemDao.getItem(1L);
        assertTrue(optionalItem.isPresent());
        Item itemUpdated = optionalItem.get();
        assertNotNull(itemUpdated);
        assertEquals(item.getItemId(),itemUpdated.getItemId());
        assertEquals(item.getName(),itemUpdated.getName());

    }

    @Test
    void deleteItem() {
        itemDao.deleteItem(1L);
//        Optional<Item> optionalItem = itemDao.getItem(1L);
     //   optionalItem.isPresent();

    }
}