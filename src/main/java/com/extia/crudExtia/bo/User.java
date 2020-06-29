package com.extia.crudExtia.bo;

import lombok.*;

import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    private String name;

    private String surname;

    private Long id;



    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
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
