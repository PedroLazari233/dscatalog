package com.pedrolazari.dscatalog.dto;

import com.pedrolazari.dscatalog.entities.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleDTO implements Serializable {

    private Long id;
    private String authority;

    public RoleDTO() {
    }

    public RoleDTO (Role role){
        id = role.getId();
        authority = role.getAuthority();
    }
}
