package com.pedrolazari.dscatalog.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class UserInsertDTO extends UserDTO{

    @Setter(AccessLevel.NONE)
    private String password;

    UserInsertDTO(){
        super();
    }
}
