package com.extia.crudExtia.services;

import com.extia.crudExtia.dao.LibraryDao;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import com.extia.crudExtia.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.google.common.collect.Lists.newArrayList;

@Service
public class LibraryService {

    @Autowired
    LibraryDao libraryDao;
    @Autowired
    ItemService itemService;


    public List<Library> getAllLibraries() {

        List<Library> libraries =  libraryDao.getAllLibraries();
        linkItemsToLibraries(libraries);

        return libraries;
    }

    public Library getLibrary(Long id) throws ResourceNotFoundException {
        Library library = libraryDao.getLibrary(id);

        Map<Long, List<Item>> itemsMap = itemService.getItemByLibraries(newArrayList(library.getLibraryId()));
        library.setItems(itemsMap.get(library.getLibraryId()));
        return library;

    }

    public List<Library> findLibraries(Library search) throws ResourceNotFoundException {
        List<Library> libraries = libraryDao.findLibraries(search);
        linkItemsToLibraries(libraries);

        return libraries;
    }


    public Map<Long,List<Library>> getLibraryByUsers(List<Long> ids) {

        List<Library> libraries = libraryDao.getLibraryByUsers(ids);
        linkItemsToLibraries(libraries);


        Map<Long, List<Library>> result = new HashMap<>();
        if(!CollectionUtils.isEmpty(libraries)) {
            for(Library library : libraries){
                if(result.get(library.getUserId()) == null) {
                    result.put(library.getUserId(), toArray(library));
                }else{
                    result.get(library.getUserId()).add(library);
                }
            }
        }
        return result;
    }

    private List<Library> toArray(Library library) {
        List<Library> list =new ArrayList<>();
        list.add(library);
        return  list;
    }

    private void linkItemsToLibraries(List<Library> libraries) {
        List<Long> ids= libraries.stream().map(Library::getLibraryId).collect(Collectors.toList());

        Map<Long, List<Item>> itemsMap = itemService.getItemByLibraries(ids);
        libraries.stream().forEach(library -> library.setItems(itemsMap.get(library.getLibraryId())));
    }

    public Library createLibrary(Library libraryToCreate) {
        libraryToCreate= libraryDao.createLibrary(libraryToCreate);
        List<Item> items = itemService.updateOrCreateItems(libraryToCreate.getItems());
        libraryToCreate.setItems(items);
        return libraryToCreate;
    }

    public List<Library> createOrUpdateLibraries(List<Library> libraries){
        List<Library>librariesToCreate = libraries.stream().filter(library -> library.getLibraryId()==null)
                .collect(Collectors.toList());
        List<Library>librariesToUpdate = libraries.stream().filter(library -> library.getLibraryId()!=null)
                .collect(Collectors.toList());
        libraries= libraryDao.createLibraries(librariesToCreate);
        libraries.addAll(libraryDao.updateLibraries(librariesToUpdate));

        libraries.forEach(library ->{
            library.getItems().forEach(item -> item.setLibraryId(library.getLibraryId()));
            library.setItems(itemService.updateOrCreateItems(library.getItems()));
        } );

        return libraries;
    }

    public Library updateLibrary(Library libraryToUpdate) {
        Library updated = libraryDao.updateLibrary(libraryToUpdate);
        updated.getItems().forEach(item -> item.setLibraryId(updated.getLibraryId()));
        updated.setItems(itemService.updateOrCreateItems(libraryToUpdate.getItems()));
        return null;
    }

    public void deleteLibrary(Long id) throws ResourceNotFoundException {
        Library libraryToDelete = getLibrary(id);
        libraryToDelete.getItems().forEach(item -> itemService.deleteItem(item.getItemId()));
        libraryToDelete.setItems(null);
        libraryDao.deleteLibrary(id);

    }
}
