package com.extia.crudExtia.controller;

import com.extia.crudExtia.enums.E_TypeItem;
import com.extia.crudExtia.exceptions.ResourceNotFoundException;
import com.extia.crudExtia.models.Item;
import com.extia.crudExtia.services.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import springfox.documentation.spring.web.json.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    ItemController itemController;

    @MockBean
    ItemService itemService;

    private List<Item> items =new ArrayList<>();
    private Item item;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks( this );
        item = Item.builder().name("name").itemId(6L).libraryId(5L).type(E_TypeItem.MANGA.getValue()).author("Leiji Matsumoto").build();

        items.add(item);
    }

    @Test
    void getAllItems() throws Exception {
        Item item2 = Item.builder().itemId(7L).libraryId(5L).build();
        List<Item> items2 =new ArrayList<>();
        items2.add(item); items2.add(item2);
        given(this.itemService.getAllItem()).willReturn(items2 );

        mvc.perform( MockMvcRequestBuilders
                .get("/items/all")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].itemId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].itemId").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].itemId").value(7L));
    }

    @Test
    void getItem() throws Exception {
        given(itemService.getItem(6L)).willReturn(item);
        mvc.perform( MockMvcRequestBuilders
                .get("/items/6")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(6L));

    }
    @Test
    void dontGetItem() throws Exception {
        given(itemService.getItem(6L)).willThrow(ResourceNotFoundException.class);
        mvc.perform( MockMvcRequestBuilders
                .get("/items/6")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @Test
    void cantGetItem() throws Exception {
        given(itemService.getItem(6L)).willThrow(ResourceNotFoundException.class);
        mvc.perform( MockMvcRequestBuilders
                .get("/items/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    void findItem()throws Exception  {
        given(itemService.searchItem(any(Item.class))).willReturn(items);
        Item item2 = Item.builder().name("name").libraryId(5L).build();
        mvc.perform( MockMvcRequestBuilders
                .post("/items/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(item2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].itemId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].itemId").value(6L));

    }

    @Test
    void createItem() throws Exception  {
        Item item2 = Item.builder().name("name").libraryId(5L).type(E_TypeItem.MANGA.getValue())
                .author("Leiji Matsumoto").build();
        given(itemService.createItem(any(Item.class))).willReturn(item);
        mvc.perform( MockMvcRequestBuilders
                .post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(item2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Leiji Matsumoto"));
    }

    @Test
    void cantCreateItem() throws Exception  {
        given(itemService.createItem(any(Item.class))).willReturn(item);
        mvc.perform( MockMvcRequestBuilders
                .post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editItem() throws Exception  {
        given(itemService.checkAndUpdateItem(any(Item.class))).willReturn(item);
        mvc.perform( MockMvcRequestBuilders
                .put("/items/6")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(item))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Leiji Matsumoto"));

    }

    @Test
    void noBodyEditItem() throws Exception  {
        given(itemService.checkAndUpdateItem(any(Item.class))).willReturn(item);
        mvc.perform( MockMvcRequestBuilders
                .put("/items/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    void deleteItem() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/items/4"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}