package com.extia.crudExtia.bo;

import lombok.*;

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

}
