package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Repository
@PropertySource("classpath:db/sql/library.properties")
public class LibraryDaoImpl implements LibraryDao {

    private static final String ID_LIBRARY ="libraryId";
    private static final String ID_USER ="userid";
    private static final String NAME_LIBRARY ="nameLibrary";


    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${library.find}")
    private String requestFindLibrary;
    @Value("${library.create}")
    private String requestCreateLibrary;
    @Value("${library.update}")
    private String requestUpdateLibrary;
    @Value("${library.delete}")
    private String requestDeleteLibrary;

    @Value("${library.where.id.clause}")
    private String whereId;
    @Value("${library.where.user.clause}")
    private String whereUserId;
    @Value("${library.where.name.clause}")
    private String whereName;
    @Value("${library.where.like.name.clause}")
    private String whereLikeName;
    @Value("${library.where.user.in.clause}")
    private String whereUserIdIn;

    @Override
    public List<Library> getAllLibraries() {
        List<Library> libraries = jdbcTemplate.query(requestFindLibrary, getLibraryRowMapper());
        return libraries;
    }

    @Override
    public Library getLibrary(Long id) throws ResourceNotFoundException {
        StringBuilder query = new StringBuilder(requestFindLibrary).append(" ").append(whereId);
        List<Library> libraries = jdbcTemplate.query(query.toString(),ImmutableMap.of(ID_LIBRARY, id),getLibraryRowMapper());
        if(CollectionUtils.isEmpty(libraries)){
            log.error("Library not found",query.toString(),id);
            throw new ResourceNotFoundException("Library not found");
        }
        return libraries.get(0);
    }

    @Override
    public List<Library> findLibraries(Library search) throws ResourceNotFoundException {
        StringBuilder query  = new StringBuilder(requestFindLibrary);
        HashMap<String, Object> params = newHashMap();

        if(search.getLibraryId()!=null){
            query.append(" ").append(whereId);
            params.put(ID_LIBRARY,search.getLibraryId());
        }

        if(StringUtils.isNotBlank(search.getName())){
            if(search.getName().contains("%")) {
                query.append(" ").append(whereLikeName);
            }else{
                query.append("").append(whereName);
            }
            params.put(NAME_LIBRARY,search.getName());
        }
        if(search.getUserId()!=null){
            query.append(" ").append(whereUserId);
            params.put(ID_USER,search.getUserId());
        }
        List<Library> libraries = jdbcTemplate.query(query.toString(),params,getLibraryRowMapper());
        if(CollectionUtils.isEmpty(libraries)){
            log.error("user not found",query.toString(),params);
            throw new ResourceNotFoundException("user not found");
        }

        return libraries;
    }

    @Override
    public List<Library> getLibraryByUsers(List<Long> ids){
        StringBuilder query  = new StringBuilder(requestFindLibrary).append(" ").append(whereUserIdIn).append("( :userid )");
        List<Library> libraries = new ArrayList<>();
        boolean done = false;
        int max = 1000;
        while(!done) {
            if (ids.size() <= 1000) {
                max =ids.size();
                done =true;
            }
            List<Long> idList = ids.subList(0, max);
            libraries.addAll( jdbcTemplate.query(query.toString(),ImmutableMap.of(ID_USER, idList),getLibraryRowMapper()));
            if(!done){
                ids = ids.subList(max,ids.size());
            }
        }

        return libraries;

    }

    @Override
    public List<Library> createLibraries(List<Library> libraries){
        libraries.forEach(library -> createLibrary(library));
        return libraries;
    }

    @Override
    public Library createLibrary(Library libraryToCreate) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(requestCreateLibrary, new MapSqlParameterSource()
                .addValue(NAME_LIBRARY,libraryToCreate.getName())
                .addValue(ID_USER,libraryToCreate.getUserId()) ,
                keyHolder
        );
        libraryToCreate.setLibraryId(keyHolder.getKey().longValue());
        return libraryToCreate;
    }

    @Override
    public Library updateLibrary(Library libraryToUpdate) {
        jdbcTemplate.update(requestUpdateLibrary, new MapSqlParameterSource()
                .addValue(NAME_LIBRARY,libraryToUpdate.getName())
                .addValue(ID_USER,libraryToUpdate.getUserId())
                .addValue(ID_LIBRARY, libraryToUpdate.getLibraryId())
        );
        return libraryToUpdate;
    }

    @Override
    public void deleteLibrary(Long id) {

        jdbcTemplate.update(requestDeleteLibrary, new MapSqlParameterSource().addValue(ID_LIBRARY, id));

    }


    private RowMapper<Library> getLibraryRowMapper() {
        return (rs, rowNum) -> Library.builder().libraryId(rs.getLong("library_id"))
                .userId(rs.getLong("fk_user_id"))
                .name(rs.getString("library_name"))
                .build();
    }

}
