package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Library;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Repository
@PropertySource("classpath:db/sql/library.properties")
public class LibraryDaoImpl implements LibraryDao {

    private static final String ID_LIBRARY ="libraryId";
    private static final String ID_USER ="userid";
    private static final String NAME_LIBRARY ="nameLibrary";
    private static final String LASTNAME_USER ="lastnameUser";


    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${user.find}")
    private String requestFindLibrary;
    @Value("${user.where.id.clause}")
    private String whereId;
    @Value("${user.where.user.clause}")
    private String whereUserId;
    @Value("${user.where.name.clause}")
    private String whereName;
    @Value("${user.where.like.name.clause}")
    private String whereLikeName;

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
            log.error("user not found",query.toString(),id);
            throw new ResourceNotFoundException("user not found");
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

    private RowMapper<Library> getLibraryRowMapper() {
        return (rs, rowNum) -> Library.builder().libraryId(rs.getLong("library_id"))
                .userId(rs.getLong("fk_user_id"))
                .name(rs.getString("library_name"))
                .build();
    }

}
