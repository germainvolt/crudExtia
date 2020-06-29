package com.extia.crudExtia.bo;

import com.extia.crudExtia.enums.E_TypeItem;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(of = {"itemId","libraryId"})
public class Item {
    private Long itemId;
    private Long libraryId;

    private String name;
    private String artist;
    private String resume;
    private String rate;
    private E_TypeItem type;
    private Integer quantity;
}
