package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.ItemDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    public List<Item> getAllItem() {
        return itemDao.getAllItems();
    }

    public Item getItem(Long id) throws ResourceNotFoundException {
        Item item = itemDao.getItem(id);
        if(item==null){
            log.error("Items not found",id);
            throw new ResourceNotFoundException("Items not found");
        }
        return item;
    }

    public List<Item> searchItem(Item search) throws ResourceNotFoundException {
        List<Item> items = itemDao.searchItem(search);
        if(CollectionUtils.isEmpty(items)){
            log.error("Items not found",search.toString());
            throw new ResourceNotFoundException("Items not found");
        }
        return items;
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

    public Item createItem(Item itemToCreate) {

        return itemDao.createItem(itemToCreate);
    }

    public Item checkAndUpdateItem(Item itemToEdit) throws ResourceNotFoundException {
        Item item = itemDao.getItem(itemToEdit.getItemId());

        if(item==null){
            log.error("Items not found",itemToEdit);
            throw new ResourceNotFoundException("Items not found");
        }

        return updateItem(itemToEdit);
    }

    public List<Item> updateOrCreateItems( List<Item> itemsToEdit){
        List<Item> result = new ArrayList<>();
        List<Long> ids= itemsToEdit.stream().map(Item::getItemId).collect(Collectors.toList());
        List<Item> itemsToCreate = itemsToEdit.stream().filter(item -> item.getItemId()==null)
                .collect(Collectors.toList());
        result.addAll(itemDao.createItems(itemsToCreate));
        List<Item> itemsToUpdate = itemsToEdit.stream().filter(item -> item.getItemId()!=null)
                .collect(Collectors.toList());
        result.addAll(itemDao.updateItems(itemsToUpdate));

        return result;

    }

    private Item updateItem(Item itemToEdit) {
        return itemDao.updateItem(itemToEdit);
    }

    public void deleteItem(Long id) {
        itemDao.deleteItem(id);
    }
}
