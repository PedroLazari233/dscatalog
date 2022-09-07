package com.pedrolazari.dscatalog.dto;

import com.pedrolazari.dscatalog.services.validation.UserInsertValid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@UserInsertValid
public class UserInsertDTO extends UserDTO{

    @Setter(AccessLevel.NONE)
    private String password;

    UserInsertDTO(){
        super();
    }
}
