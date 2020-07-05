package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;
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

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Maps.newHashMap;

@Slf4j
@Repository
@PropertySource("classpath:db/sql/user.properties")
public class UserDaoImpl implements UserDao {

    private static final String ID_USER ="idUser";
    private static final String NAME_USER ="nameUser";
    private static final String LASTNAME_USER ="lastnameUser";

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${user.find}")
    private String requestFindUser;
    @Value("${user.insert}")
    private String requestCreateUser;
    @Value("${user.update}")
    private String requestUpdateUser;
    @Value("${user.delete}")
    private String requestDeleteUser;

    @Value("${user.where.id.clause}")
    private String whereId;
    @Value("${user.where.name.clause}")
    private String whereName;
    @Value("${user.where.lastname.clause}")
    private String whereLastName;
    @Value("${user.where.like.name.clause}")
    private String whereLikeName;
    @Value("${user.where.like.lastname.clause}")
    private String whereLikeLastName;

    @Override
    public List<User> getAllUsers() {
        List<User> users =jdbcTemplate.query(requestFindUser, getUserRowMapper());
        return users;
    }

    @Override
    public User getUser(Long id) throws ResourceNotFoundException {
        StringBuilder query = new StringBuilder(requestFindUser).append(" ").append(whereId);

        List<User> users =jdbcTemplate.query(query.toString(), ImmutableMap.of(ID_USER, id),getUserRowMapper());

        if(CollectionUtils.isEmpty(users)){
            log.error("user not found",query.toString(),id);
            throw new ResourceNotFoundException("user not found");
        }
        log.error("user found",query.toString(),id);
        System.out.println(id+ " user found "+query.toString() +"\n"+users.get(0));
        return users.get(0);
    }

    @Override
    public List<User> findUsers(User search) throws ResourceNotFoundException {

        StringBuilder query = new StringBuilder(requestFindUser);
        HashMap<String, Object> params = newHashMap();

        if(StringUtils.isNotBlank(search.getName())){
            if(search.getName().contains("%")){
                query.append(" ").append(whereLikeName);
                params.put(NAME_USER,search.getName());
            }else{
                query.append(" ").append(whereName);
                params.put(NAME_USER,search.getName());
            }
        }
        if(StringUtils.isNotBlank(search.getLastname())){
            if(search.getLastname().contains("%")){
                query.append(" ").append(whereLikeLastName);
                params.put(LASTNAME_USER,search.getLastname());
            }else{
                query.append(" ").append(whereLastName);
                params.put(LASTNAME_USER,search.getLastname());
            }

        }
        if(search.getId()!=null){
            query.append(" ").append(whereId);
            params.put(ID_USER,search.getId());
        }


        List<User> users =jdbcTemplate.query(query.toString(), params,getUserRowMapper());
        if(CollectionUtils.isEmpty(users)){
            log.error("user not found",query.toString(),params);
            throw new ResourceNotFoundException("user not found");
        }
        return users;
    }

    @Override
    public User createUser(User userToCreate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(requestCreateUser, new MapSqlParameterSource()
                .addValue(NAME_USER,userToCreate.getName())
                .addValue(LASTNAME_USER,userToCreate.getLastname()) ,
                keyHolder
        );
        userToCreate.setId(keyHolder.getKey().longValue());
        return userToCreate;
    }

    @Override
    public User updateUser(User userToUpdate) {
        jdbcTemplate.update(requestUpdateUser, new MapSqlParameterSource()
                        .addValue(NAME_USER,userToUpdate.getName())
                        .addValue(LASTNAME_USER,userToUpdate.getLastname())
                        .addValue(ID_USER,userToUpdate.getId())
        );
        return userToUpdate;
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update(requestDeleteUser, new MapSqlParameterSource()
                .addValue(ID_USER,id)
        );

    }


    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> User.builder().id(rs.getLong("id"))
                .name(rs.getString("name"))
                .lastname(rs.getString("lastname"))
                .build();
    }

}
