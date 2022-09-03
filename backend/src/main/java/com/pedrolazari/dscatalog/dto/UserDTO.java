package com.pedrolazari.dscatalog.dto;

import com.pedrolazari.dscatalog.entities.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @Setter(AccessLevel.NONE)
    Set<RoleDTO> roleDTOS = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(User user){
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        user.getRoles().forEach(role -> this.roleDTOS.add(new RoleDTO(role)));
    }
}
