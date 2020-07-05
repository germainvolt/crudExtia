package com.extia.crudExtia.dao;


import com.extia.crudExtia.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    List<Item> getAllItems();

    Optional<Item> getItem(Long id);

    List<Item> searchItem(Item search);

    List<Item> getItemByLibraries(List<Long> ids);

    Item createItem(Item itemToCreate);

    Item updateItem(Item itemToEdit);


    void deleteItem(Long id);
}
