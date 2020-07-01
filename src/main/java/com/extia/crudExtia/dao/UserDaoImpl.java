package com.extia.crudExtia.dao;

import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.User;
import com.extia.crudExtia.dao.UserDao;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    /*user.find = SELECT id, name, lastname FROM USER 1=1
user.where.id.clause = AND id = :idUser
user.where.name.clause = AND name = :nameUser
user.where.lastname.clause = AND lastname = :lastnameUser
user.where.like.name.clause = AND name like :nameUser
user.where.like.lastname.clause*/

    @Value("${user.find}")
    private String requestFindUser;
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

    private RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> User.builder().id(rs.getLong("id"))
                .name(rs.getString("name"))
                .lastname(rs.getString("lastname"))
                .build();
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
            if(search.getLastname().contains("%")){
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
        List<User> users =jdbcTemplate.query(query.toString(), params,getUserRowMapper());
        if(CollectionUtils.isEmpty(users)){
            log.error("user not found",query.toString(),params);
            throw new ResourceNotFoundException("user not found");
        }
        log.error("user found",query.toString(),params);
        return users;
    }
}
