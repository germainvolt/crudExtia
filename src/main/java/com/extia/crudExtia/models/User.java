package com.extia.crudExtia.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class User {

    @Getter
    private String name;

    @Getter
    private String lastname;

    @Getter
    private Long id;

    private List<Library> libraries;

    public List<Library> getLibraries() {
        if(libraries==null)
            libraries= new ArrayList<>();
        return libraries;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + lastname + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
