package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.ItemDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    public List<Item> getAllItem() {
        return itemDao.getAllItems();
    }

    public Item getItem(Long id) throws ResourceNotFoundException {
        return itemDao.getItem(id);
    }

    public List<Item> searchItem(Item search) throws ResourceNotFoundException {
        return itemDao.searchItem(search);
    }

    public Map<Long, List<Item>> getItemByLibraries(List<Long> libraryIds) {
        List<Item> items = itemDao.getItemByLibraries(libraryIds);

        Map<Long, List<Item>> result = new HashMap<>();
        if(!CollectionUtils.isEmpty(items)) {
            for(Item item : items){
                if(result.get(item.getLibraryId()) == null) {
                    result.put(item.getLibraryId(), toArray(item));
                }else{
                    result.get(item.getLibraryId()).add(item);
                }
            }
        }
        return result;
    }
    private List<Item> toArray(Item item) {
        List<Item> list = new ArrayList<>();
        list.add(item);
        return  list;
    }
}
