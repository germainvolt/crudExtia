package com.extia.crudExtia.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"libraryId"})
public class Library {

    @Setter
    @Getter
    private Long libraryId;

    @Setter
    @Getter
    private Long userId;

    @Setter
    @Getter
    private String name;

    @Setter
    private List<Item> items;
    public List<Item> getItems() {
        if(items== null)
            items= new ArrayList<>();
        return items;
    }


    @Override
    public String toString() {
        return "Library{" +
                "libraryId=" + libraryId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
