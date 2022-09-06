package com.pedrolazari.dscatalog.resources;

import com.pedrolazari.dscatalog.dto.UserDTO;
import com.pedrolazari.dscatalog.dto.UserDTO;
import com.pedrolazari.dscatalog.dto.UserInsertDTO;
import com.pedrolazari.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value="/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable)
    {
        Page<UserDTO> userDTOS = userService.findAllPaged(pageable);
        return ResponseEntity.ok().body(userDTOS);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserInsertDTO userInsertDTO){
        UserDTO userDTO = userService.insertUser(userInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO){
        userDTO = userService.updateUser(id, userDTO);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
