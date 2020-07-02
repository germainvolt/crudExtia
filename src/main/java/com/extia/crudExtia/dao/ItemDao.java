package com.extia.crudExtia.dao;


import com.extia.crudExtia.models.Item;

import java.util.Collection;
import java.util.List;

public interface ItemDao {
    List<Item> getAllItems();

    Item getItem(Long id);

    List<Item> searchItem(Item search);

    List<Item> getItemByLibraries(List<Long> ids);

    Item createItem(Item itemToCreate);

    Item updateItem(Item itemToEdit);

    List<Item> createItems(List<Item> itemsToCreate);

    List<Item> updateItems(List<Item> itemsToUpdate);

    void deleteItem(Long id);
}
