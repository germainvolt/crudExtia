package com.extia.crudExtia.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum E_TypeItem {

    BD("BD"),COMIC("COMIC"),MANGA("MANGA"),DVD("DVD"),BLURAY("BLURAY"),CD("CD");
    private String value;

    public E_TypeItem getByValue(String value){
        Optional<E_TypeItem> typeOption = Arrays.stream(E_TypeItem.values()).filter(v-> v.getValue().equals(value)).findFirst();
        return  typeOption.isPresent()? typeOption.get():null;
    }



}
