package com.extia.crudExtia.dao;


import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Repository
@PropertySource("classpath:db/sql/item.properties")
public class ItemDaoImpl implements ItemDao {
    private static final String ID_LIBRARY ="libraryId";
    private static final String ITEM_ID ="itemid";
    private static final String NAME_ITEM ="nameitem";
    private static final String AUTHOR ="author";
    private static final String TYPE ="itemType";


    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${item.find}")
    private String requestFindItem;
    @Value("${item.where.id.clause}")
    private String whereId;
    @Value("${item.where.id.in.clause}")
    private String whereIdIn;
    @Value("${item.where.library.clause}")
    private String wherelibraryId;
    @Value("${item.where.name.clause}")
    private String whereName;
    @Value("${item.where.like.name.clause}")
    private String whereLikeName;
    @Value("${item.where.author.clause}")
    private String whereAuthor;
    @Value("${item.where.like.author.clause}")
    private String whereLikeAuthor;
    @Value("${item.where.library.in.clause}")
    private String wherelibraryIdIn;
    @Value("${item.where.type.clause}")
    private String whereType;


    @Value("${item.insert}")
    private String requestInsertItem;
    @Value("${item.update}")
    private String requestUpdateItem;
    @Value("${item.delete}")
    private String requestDeleteItem;

    @Override
    public List<Item> getAllItems() {

        List<Item> items = jdbcTemplate.query(requestFindItem, getItemRowMapper());

        return items;
    }

    private RowMapper<Item> getItemRowMapper() {
        return (rs, rowNum) -> Item.builder()
                .itemId(rs.getLong("ITEM_ID"))
                .libraryId(rs.getLong("FK_LIBRARY_ID"))
                .name(rs.getString("ITEM_NAME"))
                .author(rs.getString("ITEM_AUTHOR"))
                .type(rs.getString("ITEM_TYPE")).build();
    }

    @Override
    public Optional<Item> getItem(Long id) {
        try {
            StringBuilder query = new StringBuilder(requestFindItem).append(" ").append(whereId);
            Item item = jdbcTemplate.queryForObject(query.toString(),
                    ImmutableMap.of(ITEM_ID, id), getItemRowMapper());

            return Optional.ofNullable(item);

        }catch (EmptyResultDataAccessException e){
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Item> searchItem(Item search) {
        StringBuilder query  = new StringBuilder(requestFindItem);
        HashMap<String, Object> params = new HashMap();

        if(search.getItemId()!=null){
            query.append(" ").append(whereId);
            params.put(ITEM_ID,search.getItemId());
        }
        if(search.getLibraryId()!=null){
            query.append(" ").append(wherelibraryId);
            params.put(ID_LIBRARY,search.getLibraryId());
        }
        if(StringUtils.isNotBlank(search.getType())){
            query.append(" ").append(whereType);
            params.put(TYPE,search.getType());
        }
        if(StringUtils.isNotBlank(search.getName())){
            if(search.getName().contains("%")) {
                query.append(" ").append(whereLikeName);
            }else{
                query.append(" ").append(whereName);
            }
            params.put(NAME_ITEM,search.getName());
        }
        if(StringUtils.isNotBlank(search.getAuthor())){
            if(search.getAuthor().contains("%")) {
                query.append(" ").append(whereLikeAuthor);
            }else{
                query.append(" ").append(whereAuthor);
            }
            params.put(AUTHOR,search.getAuthor());
        }
        List<Item> items = jdbcTemplate.query(query.toString(),params, getItemRowMapper());


        return items;
    }

    @Override
    public List<Item> getItemByLibraries(List<Long> ids) {
        StringBuilder query  = new StringBuilder(requestFindItem).append(" ").append(wherelibraryIdIn).append("( :libraryId )");
        List<Item> items = searchItemsByIds(ids, query, ID_LIBRARY);
        return items;
    }

    @Override
    public Item createItem(Item itemToCreate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(requestInsertItem, new MapSqlParameterSource()
                .addValue(ID_LIBRARY, itemToCreate.getLibraryId())
                .addValue(NAME_ITEM,itemToCreate.getName())
                .addValue(AUTHOR, itemToCreate.getAuthor())
                .addValue(TYPE, itemToCreate.getType()),
                keyHolder
        );
        itemToCreate.setItemId(keyHolder.getKey().longValue());
        return itemToCreate;
    }

    @Override
    public Item updateItem(Item itemToEdit) {
        jdbcTemplate.update(requestUpdateItem, new MapSqlParameterSource()
                .addValue(ID_LIBRARY, itemToEdit.getLibraryId())
                .addValue(NAME_ITEM,itemToEdit.getName())
                .addValue(AUTHOR, itemToEdit.getAuthor())
                .addValue(TYPE, itemToEdit.getType())
                .addValue(ITEM_ID,itemToEdit.getItemId())
        );
        return itemToEdit;
    }

    @Override
    public void deleteItem(Long id) {
        jdbcTemplate.update(requestDeleteItem, new MapSqlParameterSource()
                .addValue(ITEM_ID,id)
        );
    }


    private List<Item> searchItemsByIds(List<Long> ids, StringBuilder query, String searchedId) {
        List<Item> items = new ArrayList<>();

        boolean done = false;
        int max = 1000;
        while(!done) {
            if (ids.size() <= 1000) {
                max =ids.size();
                done =true;
            }
            List<Long> idList = ids.subList(0, max);
            items.addAll( jdbcTemplate.query(query.toString(), ImmutableMap.of(searchedId, idList),getItemRowMapper()));
            if(!done){
                ids = ids.subList(max,ids.size());
            }
        }
        return items;
    }

}
