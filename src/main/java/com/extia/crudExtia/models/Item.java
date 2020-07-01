package com.extia.crudExtia.models;

import com.extia.crudExtia.enums.E_TypeItem;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = {"itemId","libraryId"})
public class Item {
    @Setter
    private Long itemId;
    @Setter
    private Long libraryId;

    @Setter
    private String name;
    @Setter
    private String author;
    private String type;

    public void setType(E_TypeItem type){
        this.type=type.toString();
    }
    public void setType(String type){
        this.type=E_TypeItem.getByValue(type)==null?null:type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", libraryId=" + libraryId +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
