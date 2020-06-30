package com.extia.crudExtia.bo;

import lombok.*;

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
    @Getter
    private List<Item> items;

}
