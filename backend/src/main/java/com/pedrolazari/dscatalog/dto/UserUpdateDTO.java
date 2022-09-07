package com.pedrolazari.dscatalog.dto;

import com.pedrolazari.dscatalog.services.validation.UserInsertValid;
import com.pedrolazari.dscatalog.services.validation.UserUpdateValid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO{

}
