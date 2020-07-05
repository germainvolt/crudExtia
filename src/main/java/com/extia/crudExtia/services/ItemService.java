package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.ItemDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    public List<Item> getAllItem() {
        return itemDao.getAllItems();
    }

    public Item getItem(Long id) throws ResourceNotFoundException {
        Optional<Item> itemOptional = itemDao.getItem(id);
        if(!itemOptional.isPresent()){
            throw new ResourceNotFoundException("Items not found");
        }
        return itemOptional.get();
    }

    public List<Item> searchItem(Item search) throws ResourceNotFoundException {
        List<Item> items = itemDao.searchItem(search);
        if(CollectionUtils.isEmpty(items)){
            log.error("Items not found",search.toString());
            throw new ResourceNotFoundException("Items not found");
        }
        return items;
    }

    public List<Item> getItemsByLibrary(Long libraryId){
        return itemDao.getItemByLibraries(newArrayList(libraryId));
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
        Optional<Item> itemOptional = itemDao.getItem(itemToEdit.getItemId());
        if(!itemOptional.isPresent()){
            throw new ResourceNotFoundException("Items not found");
        }
        return updateItem(itemToEdit);
    }

    public List<Item> updateOrCreateItems( List<Item> itemsToEdit){
        itemsToEdit.stream().filter(item -> item.getItemId()==null).forEach(
                item ->  itemDao.createItem(item));

        itemsToEdit.stream().filter(item -> item.getItemId()!=null)
                .forEach(item -> updateItem(item));

        return itemsToEdit;

    }

    private Item updateItem(Item itemToEdit) {
        return itemDao.updateItem(itemToEdit);
    }

    public void deleteItem(Long id) throws ResourceNotFoundException {
        getItem(id);
        itemDao.deleteItem(id);
    }

    public void deleteItem(Item item) {
        itemDao.deleteItem(item.getItemId());
    }
}
