package com.pedrolazari.dscatalog.entities;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Category implements Serializable {

    private Long id;
    private String name;
}
